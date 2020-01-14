package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.GeomesaFeature;
import com.cnhindustrial.telemetry.common.model.ControllerDto;
import com.cnhindustrial.telemetry.function.DeserializeControllerDataFunction;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Temporary pipeline to test reading of Controller Data.
 */
public class ControllerDataPipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerDataPipeline.class);

    private final DataStreamSource<byte[]> controllerDataSource;
    private final SinkFunction<GeomesaFeature> controllerDataSink;

    ControllerDataPipeline(DataStreamSource<byte[]> controllerDataSource, SinkFunction<GeomesaFeature> controllerDataSink) {
        this.controllerDataSource = controllerDataSource;
        this.controllerDataSink = controllerDataSink;
    }

    public static void main(String[] args) throws Exception {
        ParameterTool parameters = new ParameterToolBuilder()
                .mergeEnvironmentVariables()
                .mergeCommandLineArguments(args)
                .build();

        StreamExecutionEnvironment see = StreamExecutionEnvironment.getExecutionEnvironment();
        FunctionFactory functionFactory = new FunctionFactory(parameters);

        ControllerDataPipeline controllerDataPipeline = new ControllerDataPipeline(
                functionFactory.getControllerDataSource(see),
                new PrintSinkFunction<>());

        controllerDataPipeline.build(see);
        controllerDataPipeline.execute(see);
    }

    void build(StreamExecutionEnvironment see) {
        DataStream<byte[]> sourceStream = controllerDataSource
                .name("Bytes from Files");

        DataStream<ControllerDto> controllerDataStream = sourceStream
                .map(new DeserializeControllerDataFunction())
                .name("Deserialize Controller Data");

        // TODO: temporarily commented out
        /*DataStream<String> stringDataStream = controllerDataStream
                .map(ControllerDto::toString)
                .name("Controller Dto to String");

        stringDataStream
                .addSink(controllerDataSink)
                .name("Std Out");*/
    }

    void execute(StreamExecutionEnvironment see) throws Exception {
        LOGGER.debug("Starting pipeline execution.");
        see.execute("Controller Data Pipeline");
    }
}
