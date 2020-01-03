package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for different {@link SourceFunction} and {@link SinkFunction} that serve as entry and exit points for Flink pipeline.
 */
public class FunctionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionFactory.class);

    private final ParameterTool parameters;

    public FunctionFactory(ParameterTool parameters) {
        this.parameters = parameters;
    }

    SourceFunction<TelemetryDto> getTelemetryDataSource() {
        if (parameters.has("event.hub.telemetry.endpoint")) {
            LOGGER.info("Building Telemetry Data source function connected to Azure Event Hub.");

            return new KafkaConnector.Builder()
                    .buildEventHubConnector(parameters.get("event.hub.telemetry.endpoint"))
                    .telemetrySource();
        } else {
            LOGGER.info("Building Telemetry Data source function connected to local Kafka.");

            return new KafkaConnector.Builder()
                    .buildKafkaConnector("users", "localhost:29092")
                    .telemetrySource();
        }
    }

    DataStreamSource<String> getControllerDataSource(StreamExecutionEnvironment see) {
        LOGGER.info("Building Controller Data source function connected to Azure Blob Storage.");
        // TODO return see.readTextFile(parameters.get("blob.storage.controller.data"));
        return null;
    }

    SinkFunction<TelemetryDto> getDeadLetterSink() {
        LOGGER.info("Building Dead Letter Queue sink function.");
        // TODO add real implementation
        return new PrintSinkFunction<>();
    }

    SinkFunction<String> getMachineDataSink() {
        LOGGER.info("Building Machine Data sink function connected to Geomesa.");
        // TODO add real implementation
        return new PrintSinkFunction<>();
    }
}
