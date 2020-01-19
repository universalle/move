package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.common.model.TelemetryMessage;
import com.cnhindustrial.telemetry.function.MessageDeserializeFunction2;
import com.cnhindustrial.telemetry.function.ProcessWindowFunction2;
import com.cnhindustrial.telemetry.function.TelemetryKeySelector2;
import com.cnhindustrial.telemetry.model.TelemetryFeatureWrapper;
import com.twitter.chill.java.UnmodifiableMapSerializer;

import de.javakaffee.kryoserializers.CollectionsSingletonListSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.geotools.util.UnmodifiableArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;

public class IngestPipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngestPipeline.class);

    private final SourceFunction<byte[]> telemetryDataSource;
    private final DataStreamSource<byte[]> controllerDataSource;
    private final SinkFunction<TelemetryFeatureWrapper> machineDataSink;
    private final SinkFunction<TelemetryDto> deadLetterSink;

    IngestPipeline(SourceFunction<byte[]> telemetryDataSource,
                   DataStreamSource<byte[]> controllerDataSource,
                   SinkFunction<TelemetryFeatureWrapper> machineDataSink,
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
        configureExecutionEnvironment(see.getConfig(), parameters);
        see.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        FunctionFactory functionFactory = new FunctionFactory(parameters);

        IngestPipeline ingestPipeline = new IngestPipeline(
                functionFactory.getTelemetryDataSource(),
                functionFactory.getControllerDataSource(see),
                functionFactory.getMachineDataSink(),
                functionFactory.getDeadLetterSink());

        ingestPipeline.build(see);
        ingestPipeline.execute(see);
    }

    static void configureExecutionEnvironment(ExecutionConfig execConfig, ParameterTool parameters) throws Exception {
        execConfig.setGlobalJobParameters(parameters);

        Class<?> unmodifiableCollection = Class.forName("java.util.Collections$UnmodifiableCollection");
        execConfig.addDefaultKryoSerializer(unmodifiableCollection, UnmodifiableCollectionsSerializer.class);

        Class<?> unmodifiableMap = Class.forName("java.util.Collections$UnmodifiableMap");
        execConfig.addDefaultKryoSerializer(unmodifiableMap, UnmodifiableMapSerializer.class);

        execConfig.addDefaultKryoSerializer(UnmodifiableArrayList.class, CollectionsSingletonListSerializer.class);
    }

    /**
     * Build the pipeline.
     */
    void build(StreamExecutionEnvironment see) {
        LOGGER.debug("Building Ingest Pipeline");

        DataStream<byte[]> rawMessageStream = see
                .addSource(telemetryDataSource)
                .name("Message From Event Hub")
                .uid("eventhub-source")
                .setParallelism(1);

        DataStream<byte[]> rawControllerStream = controllerDataSource
                .name("Message from Blob Storage")
                .uid("blob-storage-source");

        MessageDeserializeFunction2 messageDeserializeFunction = new MessageDeserializeFunction2();

        DataStream<TelemetryMessage> statusStream = rawMessageStream
                .process(messageDeserializeFunction)
                .name("Deserialize Telemetry")
                .uid("deserialize-telemetry")
                .rebalance();

        DataStream<TelemetryDto> process = statusStream
                .assignTimestampsAndWatermarks(new StatusWatermarkAssigner2())
                .keyBy(new TelemetryKeySelector2())
                .window(EventTimeSessionWindows.withGap(Time.milliseconds(100)))
//                .window(new EventTimeSessionWindows2(3050))
//                .trigger(CountTrigger.of(3))
                .process(new ProcessWindowFunction2())
                .setParallelism(4);

//        DataStream<TelemetryRecord> telemetryStream = statusStream.getSideOutput(messageDeserializeFunction.getSideStreamTag());
//
//        SingleOutputStreamOperator<TelemetryDto> process = statusStream
//                .assignTimestampsAndWatermarks(new StatusWatermarkAssigner())
//                .keyBy(new StatusKeySelector())
//                .connect(telemetryStream.keyBy(new TelemetryKeySelector()))
//                .flatMap(new TelemetryCoFlatMapFunction());
//                .process(new TelemetryTimeJoinFunction());

        process.addSink(deadLetterSink);

//        SingleOutputStreamOperator<ControllerDto> controllerStream = rawControllerStream
//                .map(new DeserializeMapFunction<>(ControllerDto.class))
//                .name("Deserialize Controller Data")
//                .uid("deserialize-controller");

//        StreamTableSource<FlattenTelemetryDto> telemetryTableSource = new FlattenTelemetryTableSource(flattenTelemetryStream);
//        StreamTableSource<ControllerDto> controllerTableSource = new ControllerTableSource(controllerStream);

//        StreamTableEnvironment ste = StreamTableEnvironment.create(see);

//        Table telemetryTable = ste.fromDataStream(flattenTelemetryStream, "deviceid, assetId");
//        Table controllerTable = ste.fromDataStream(controllerStream, "id, description");

//        Table mergedTable = controllerTable.join(telemetryTable, "id = assetId"); // TODO the join condition is wrong

//        DataStream<ControllerDto> controllerMergeStream = ste.toAppendStream(mergedTable, ControllerDto.class);

//        DataStream<TelemetryFeatureWrapper> telemetryFeatureStream = process
//                .map(new TelemetryConverter())
//                .name("Telemetry Feature converter")
//                .uid("telemetry-feature-converter");
//
//        telemetryFeatureStream.addSink(machineDataSink)
//                .name("Sink Telemetry data to Buffered List")
//                .uid("telemetry-geomesa-sink");
//
//        DataStream<SimpleFeatureImpl> controllerFeatureStream = controllerStream
//                .map(new GeomesaControllerFeatureConverter())
//                .name("Controller Feature converter")
//                .uid("controller-feature-converter");
//
//        controllerFeatureStream.addSink(new PrintSinkFunction<>())
//                .name("Sink Controller data to Buffered List")
//                .uid("controller-geomesa-sink");
    }

    /**
     * Execute the pipeline.
     */
    void execute(StreamExecutionEnvironment see) throws Exception {
        LOGGER.debug("Starting pipeline execution.");
        see.execute("Ingest Pipeline");
    }
}
