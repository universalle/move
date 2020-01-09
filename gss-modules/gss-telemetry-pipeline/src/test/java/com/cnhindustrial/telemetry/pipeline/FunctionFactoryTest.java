package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FunctionFactoryTest {

    @Test
    void getTelemetryDataSourceKafka() {
        ParameterTool parameters = ParameterTool.fromMap(emptyMap());
        SourceFunction<TelemetryDto> telemetryDataSource = new FunctionFactory(parameters).getTelemetryDataSource();

        assertThat(telemetryDataSource, Matchers.instanceOf(FlinkKafkaConsumer.class));
    }

    @Test
    void getTelemetryDataSourceEventHub() {
        ParameterTool parameters = ParameterTool.fromMap(singletonMap(
                "event.hub.telemetry.endpoint", "Endpoint=sb://flink-poc.servicebus.windows.net/;;;EntityPath=atqa-test"));
        SourceFunction<TelemetryDto> telemetryDataSource = new FunctionFactory(parameters).getTelemetryDataSource();

        assertThat(telemetryDataSource, Matchers.instanceOf(FlinkKafkaConsumer.class));
    }

    @Disabled("Need to mock fields")
    @Test
    void getControllerDataSource() {
        StreamExecutionEnvironment see = mock(StreamExecutionEnvironment.class);

        ParameterTool parameters = ParameterTool.fromMap(singletonMap(
                "blob.storage.controller.data", "blob.storage.controller.data.value"));
        FunctionFactory functionFactory = new FunctionFactory(parameters);
        functionFactory.getControllerDataSource(see);

        verify(see).readTextFile("blob.storage.controller.data.value");
    }

    @Test
    void getDeadLetterSink() {
        SinkFunction<TelemetryDto> deadLetterSink =
                new FunctionFactory(ParameterTool.fromMap(emptyMap())).getDeadLetterSink();
        assertThat(deadLetterSink, Matchers.instanceOf(PrintSinkFunction.class));
    }

    @Test
    void getMachineDataSink() {
        SinkFunction<String> machineDataSink =
                new FunctionFactory(ParameterTool.fromMap(emptyMap())).getMachineDataSink();
        assertThat(machineDataSink, Matchers.instanceOf(PrintSinkFunction.class));
    }
}