package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngestPipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngestPipeline.class);

    private final SourceFunction<TelemetryDto> telemetrySource;
    private final SinkFunction<String> sinkFunction;

    public static void main(String[] args) throws Exception {

        ParameterTool parameters = new ParameterToolBuilder()
                .mergeEnvironmentVariables()
                .mergeCommandLineArguments(args)
                .build();


        SourceFunction<TelemetryDto> telemetrySource;
        if (parameters.has("event.hub.telemetry.endpoint")) {
            telemetrySource = new KafkaConnector.Builder()
                    .buildEventHubConnector(parameters.get("event.hub.telemetry.endpoint"))
                    .telemetrySource();
        } else {
            telemetrySource = new KafkaConnector.Builder()
                    .buildKafkaConnector("users", "localhost:29092")
                    .telemetrySource();
        }
        SinkFunction<String> sinkFunction = new PrintSinkFunction<>();

        IngestPipeline ingestPipeline = new IngestPipeline(telemetrySource, sinkFunction);
        ingestPipeline.buildAndExecute();
    }

    public IngestPipeline(SourceFunction<TelemetryDto> telemetrySource, SinkFunction<String> sinkFunction) {
        this.telemetrySource = telemetrySource;
        this.sinkFunction = sinkFunction;
    }

    private void buildAndExecute() throws Exception {
        LOGGER.debug("Building Ingest Pipeline");
        StreamExecutionEnvironment see = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<TelemetryDto> fromEventHub = see
                .addSource(telemetrySource)
                .name("Get Messages From Event Hub")
                .uid("message-source")
                .returns(TelemetryDto.class)
                .rebalance();

        MapFunction<TelemetryDto, String> mapFunction = new TelMapFunc();
        fromEventHub.map(mapFunction)
                .returns(Types.STRING)
                .addSink(sinkFunction);

        LOGGER.debug("Starting pipeline execution.");
        see.execute("Ingest Pipeline");
    }

    private static class TelMapFunc implements MapFunction<TelemetryDto, String> {
        public String map(TelemetryDto telemetryDto) throws Exception {
            return telemetryDto.toString();
        }
    }
}
