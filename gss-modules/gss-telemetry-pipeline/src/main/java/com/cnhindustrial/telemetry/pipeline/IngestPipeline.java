package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.groovy.GroovyScript;
import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.function.DeserializeMapFunction;
import com.cnhindustrial.telemetry.function.DynamicValidationFunction;
import com.cnhindustrial.telemetry.model.TelemetryFeatureWrapper;
import com.twitter.chill.java.UnmodifiableMapSerializer;

import de.javakaffee.kryoserializers.CollectionsSingletonListSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.OutputTag;
import org.geotools.util.UnmodifiableArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngestPipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngestPipeline.class);

    private final SourceFunction<byte[]> telemetryDataSource;
    private final DataStreamSource<byte[]> controllerDataSource;
    private final DataStreamSource<GroovyScript> dynamicValidationSource;
    private final SinkFunction<TelemetryFeatureWrapper> machineDataSink;
    private final SinkFunction<TelemetryDto> deadLetterSink;
    private final SinkFunction<TelemetryDto> logTelemetry;

    public static final MapStateDescriptor<String, GroovyScript> DYNAMIC_VALIDATION_DESCRIPTOR = new MapStateDescriptor(
            "scripts",
            BasicTypeInfo.STRING_TYPE_INFO,
            TypeInformation.of(GroovyScript.class)
    );

    IngestPipeline(SourceFunction<byte[]> telemetryDataSource,
                   DataStreamSource<byte[]> controllerDataSource,
                   DataStreamSource<GroovyScript> dynamicValidationSource,
                   SinkFunction<TelemetryFeatureWrapper> machineDataSink,
                   SinkFunction<TelemetryDto> deadLetterSink,
                   SinkFunction<TelemetryDto> logTelemetry) {
        this.telemetryDataSource = telemetryDataSource;
        this.controllerDataSource = controllerDataSource;
        this.dynamicValidationSource = dynamicValidationSource;
        this.machineDataSink = machineDataSink;
        this.deadLetterSink = deadLetterSink;
        this.logTelemetry = logTelemetry;
    }

    public static void main(String[] args) throws Exception {

        ParameterTool parameters = new ParameterToolBuilder()
                .mergeEnvironmentVariables()
                .mergeCommandLineArguments(args)
                .build();

        StreamExecutionEnvironment see = StreamExecutionEnvironment.getExecutionEnvironment();
        configureExecutionEnvironment(see.getConfig(), parameters);

        FunctionFactory functionFactory = new FunctionFactory(parameters);

        IngestPipeline ingestPipeline = new IngestPipeline(
                functionFactory.getTelemetryDataSource(),
                functionFactory.getControllerDataSource(see),
                functionFactory.getDynamicValidationSource(see),
                functionFactory.getMachineDataSink(),
                functionFactory.getDeadLetterSink(),
                new DiscardingSink<>());

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
                .uid("eventhub-source");

//        DataStream<byte[]> rawControllerStream = controllerDataSource
//                .name("Message from Blob Storage")
//                .uid("blob-storage-source");

        OutputTag<TelemetryDto> deadLetterOutput = new OutputTag<>("invalid-messages", TypeInformation.of(TelemetryDto.class));

        SingleOutputStreamOperator<TelemetryDto> deserializedTelemetry = rawMessageStream
                .map(new DeserializeMapFunction<>(TelemetryDto.class))
                .name("Deserialize Telemetry")
                .uid("deserialize-telemetry");

        BroadcastStream<GroovyScript> broadcast = dynamicValidationSource.broadcast(DYNAMIC_VALIDATION_DESCRIPTOR);

        deserializedTelemetry
                .keyBy(new KeySelector<TelemetryDto, String>() {
                    private static final long serialVersionUID = -8796407799015234967L;

                    @Override
                    public String getKey(TelemetryDto telemetryDto) throws Exception {
                        return telemetryDto.getAssetId();
                    }
                })
                .connect(broadcast)
                .process(new DynamicValidationFunction(deadLetterOutput));

//        SingleOutputStreamOperator<TelemetryDto> validatedTelemetryStream = deserialize_telemetry
//                .process(new TelemetryValidationFunction(outputTag))
//                .name("Telemetry Validation Output")
//                .uid("telemetry-validation-output");

        deserializedTelemetry.print();

//        validatedTelemetryStream.getSideOutput(outputTag)
//                .addSink(deadLetterSink)
//                .name("Sink Invalid Telemetry data to Dead Letter Queue")
//                .uid("dead-letter-queue-sink");

//        outputTag = new OutputTag<>("telemetry-cache", TypeInformation.of(TelemetryDto.class));
//        SingleOutputStreamOperator<TelemetryDto> flattenTelemetryStream = validatedTelemetryStream
//                .process(new SideOutputProcessFunction<>(outputTag, new TelemetryDtoConverter(), TelemetryDto.class))
//                .name("Telemetry Side Output")
//                .uid("telemetry-side-output");

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

//        DataStream<TelemetryFeatureWrapper> telemetryFeatureStream = flattenTelemetryStream.getSideOutput(outputTag)
//                .map(new TelemetryConverter())
//                .name("Telemetry Feature converter")
//                .uid("telemetry-feature-converter");
//
//        telemetryFeatureStream.addSink(machineDataSink)
//                .name("Sink Telemetry data to Buffered List")
//                .uid("telemetry-geomesa-sink");

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
