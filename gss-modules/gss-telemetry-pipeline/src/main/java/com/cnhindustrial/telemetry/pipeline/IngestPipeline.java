package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.GeomesaFeature;
import com.cnhindustrial.telemetry.common.model.ControllerDto;
import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.converter.GeomesaControllerFeatureConverter;
import com.cnhindustrial.telemetry.converter.GeomesaFeatureConverter;
import com.cnhindustrial.telemetry.function.DeserializeMapFunction;
import com.cnhindustrial.telemetry.function.SideOutputProcessFunction;
import com.cnhindustrial.telemetry.function.TelemetryDtoConverter;
import com.twitter.chill.java.UnmodifiableMapSerializer;

import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.OutputTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngestPipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngestPipeline.class);

    private final SourceFunction<byte[]> telemetryDataSource;
    private final DataStreamSource<byte[]> controllerDataSource;
    private final SinkFunction<GeomesaFeature> machineDataSink;
    private final SinkFunction<TelemetryDto> deadLetterSink;

    IngestPipeline(SourceFunction<byte[]> telemetryDataSource,
                   DataStreamSource<byte[]> controllerDataSource,
                   SinkFunction<GeomesaFeature> machineDataSink,
                   SinkFunction<TelemetryDto> deadLetterSink) {
        this.telemetryDataSource = telemetryDataSource;
        this.controllerDataSource = controllerDataSource;
        this.machineDataSink = machineDataSink;
        this.deadLetterSink = deadLetterSink;
    }

    public static void main(String[] args) throws Exception {

        ParameterTool parameters = new ParameterToolBuilder()
                .mergeEnvironmentVariables()
                .mergeCommandLineArguments(args)
                .build();

        StreamExecutionEnvironment see = StreamExecutionEnvironment.getExecutionEnvironment();

        see.getConfig().setGlobalJobParameters(parameters);
        Class<?> unmodColl = Class.forName("java.util.Collections$UnmodifiableCollection");
        see.getConfig().addDefaultKryoSerializer(unmodColl, UnmodifiableCollectionsSerializer.class);
        Class<?> unmodMap = Class.forName("java.util.Collections$UnmodifiableMap");
        see.getConfig().addDefaultKryoSerializer(unmodMap, UnmodifiableMapSerializer.class);

        FunctionFactory functionFactory = new FunctionFactory(parameters);

        IngestPipeline ingestPipeline = new IngestPipeline(
                functionFactory.getTelemetryDataSource(),
                functionFactory.getControllerDataSource(see),
                new PrintSinkFunction<>(), // TODO functionFactory.getMachineDataSink(),
                functionFactory.getDeadLetterSink());

        ingestPipeline.build(see);
        ingestPipeline.execute(see);
    }

    /**
     * Build the pipeline.
     */
    void build(StreamExecutionEnvironment see) {
        LOGGER.debug("Building Ingest Pipeline");

        DataStream<byte[]> rawMessageStream = see
                .addSource(telemetryDataSource)
                .name("Message From Event Hub")
                .uid("eventhub-source");

        DataStream<byte[]> rawControllerStream = controllerDataSource
                .name("Message from Blob Storage")
                .uid("blob-storage-source");

        OutputTag<TelemetryDto> outputTag = new OutputTag<>("telemetry-cache", TypeInformation.of(TelemetryDto.class));

        SingleOutputStreamOperator<TelemetryDto> flattenTelemetryStream = rawMessageStream
                .map(new DeserializeMapFunction<>(TelemetryDto.class))
                .name("Deserialize Telemetry")
                .uid("deserialize-telemetry")

                .process(new SideOutputProcessFunction<>(outputTag, new TelemetryDtoConverter(), TelemetryDto.class))
                .name("Telemetry Side Output")
                .uid("telemetry-side-output");

        SingleOutputStreamOperator<ControllerDto> controllerStream = rawControllerStream
                .map(new DeserializeMapFunction<>(ControllerDto.class))
                .name("Deserialize Controller Data")
                .uid("deserialize-controller");

//        StreamTableSource<FlattenTelemetryDto> telemetryTableSource = new FlattenTelemetryTableSource(flattenTelemetryStream);
//        StreamTableSource<ControllerDto> controllerTableSource = new ControllerTableSource(controllerStream);

//        StreamTableEnvironment ste = StreamTableEnvironment.create(see);

//        Table telemetryTable = ste.fromDataStream(flattenTelemetryStream, "deviceid, assetId");
//        Table controllerTable = ste.fromDataStream(controllerStream, "id, description");

//        Table mergedTable = controllerTable.join(telemetryTable, "id = assetId"); // TODO the join condition is wrong

//        DataStream<ControllerDto> controllerMergeStream = ste.toAppendStream(mergedTable, ControllerDto.class);

        DataStream<GeomesaFeature> telemetryFeatureStream = flattenTelemetryStream.getSideOutput(outputTag)
                .map(new GeomesaFeatureConverter())
                .name("Telemetry Feature converter")
                .uid("telemetry-feature-converter");

        telemetryFeatureStream.addSink(machineDataSink)
                .name("Sink Telemetry data to Buffered List")
                .uid("telemetry-geomesa-sink");

        DataStream<GeomesaFeature> controllerFeatureStream = controllerStream
                .map(new GeomesaControllerFeatureConverter())
                .name("Controller Feature converter")
                .uid("controller-feature-converter");

        controllerFeatureStream.addSink(new PrintSinkFunction<>())
                .name("Sink Controller data to Buffered List")
                .uid("controller-geomesa-sink");
    }

    /**
     * Execute the pipeline.
     */
    void execute(StreamExecutionEnvironment see) throws Exception {
        LOGGER.debug("Starting pipeline execution.");
        see.execute("Ingest Pipeline");
    }
}
