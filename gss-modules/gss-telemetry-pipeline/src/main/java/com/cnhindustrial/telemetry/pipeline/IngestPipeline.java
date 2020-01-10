package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import com.cnhindustrial.telemetry.function.DeserializeTelemetryDataFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngestPipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngestPipeline.class);

    private final SourceFunction<String> telemetryDataSource;
    private final DataStreamSource<byte[]> controllerDataSource;
    private final SinkFunction<String> machineDataSink;
    private final SinkFunction<TelemetryDto> deadLetterSink;

    IngestPipeline(SourceFunction<String> telemetryDataSource,
                   DataStreamSource<byte[]> controllerDataSource,
                   SinkFunction<String> machineDataSink,
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
        FunctionFactory functionFactory = new FunctionFactory(parameters);

        IngestPipeline ingestPipeline = new IngestPipeline(
                functionFactory.getTelemetryDataSource(),
                functionFactory.getControllerDataSource(see),
                functionFactory.getMachineDataSink(),
                functionFactory.getDeadLetterSink());

        ingestPipeline.build(see);
        ingestPipeline.execute(see);
    }

    /**
     * Build the pipeline.
     */
    void build(StreamExecutionEnvironment see) {
        LOGGER.debug("Building Ingest Pipeline");

        DataStream<TelemetryDto> fromEventHub = see
                .addSource(telemetryDataSource)
                .name("Telemetry messages From Event Hub")
                .uid("message-source")
                .map(new DeserializeTelemetryDataFunction())
                .rebalance();

        DataStream<String> mappedStream = fromEventHub
                .map(TelemetryDto::toString)
                .setParallelism(16)
                .returns(Types.STRING);

        mappedStream
                .addSink(machineDataSink)
                .name("Machine Data to Geomesa")
                .setParallelism(1);
    }

    /**
     * Execute the pipeline.
     */
    void execute(StreamExecutionEnvironment see) throws Exception {
        LOGGER.debug("Starting pipeline execution.");
        see.execute("Ingest Pipeline");
    }
}
