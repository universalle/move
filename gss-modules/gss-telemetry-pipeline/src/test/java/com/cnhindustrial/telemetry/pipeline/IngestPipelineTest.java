package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.function.ReadLinesSourceFunction;
import com.cnhindustrial.telemetry.test.MachineDataCollectSink;
import com.cnhindustrial.telemetry.test.MiniClusterWithClientResourceExtension;
import com.twitter.chill.java.UnmodifiableMapSerializer;

import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MiniClusterWithClientResourceExtension.class)
class IngestPipelineTest {

    private StreamExecutionEnvironment see;
    private MachineDataCollectSink machineDataCollectSink;
    private FunctionFactory functionFactory;
    private SourceFunction<byte[]> telemetrySource;

    @BeforeEach
    void setUp() throws ClassNotFoundException {
        see = StreamExecutionEnvironment.getExecutionEnvironment();

        Class<?> unmodColl = Class.forName("java.util.Collections$UnmodifiableCollection");
        see.getConfig().addDefaultKryoSerializer(unmodColl, UnmodifiableCollectionsSerializer.class);
        Class<?> unmodMap = Class.forName("java.util.Collections$UnmodifiableMap");
        see.getConfig().addDefaultKryoSerializer(unmodMap, UnmodifiableMapSerializer.class);

        // configure your test environment
        see.setParallelism(32);
        see.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        see.setStateBackend((StateBackend) new MemoryStateBackend());

        Map<String, String> map = new HashMap<>();
        map.put("blob.storage.controller.data.path", "src/test/resources/com/cnhindustrial/telemetry/data/controller");
        map.put("environment.test", "true");
        ParameterTool parameters = ParameterTool
                .fromMap(map);
        functionFactory = new FunctionFactory(parameters);

        telemetrySource = new ReadLinesSourceFunction(
                "src/test/resources/com/cnhindustrial/telemetry/data/telemetry/retrofit_d2c_decoded.txt");

        machineDataCollectSink = new MachineDataCollectSink();
    }

    @AfterEach
    void tearDown() {
        machineDataCollectSink.clear();
    }

    @Test
    void testThreeTelemetryItemsInPipeline() throws Exception {
        IngestPipeline ingestPipeline = new IngestPipeline(
                telemetrySource,
                functionFactory.getControllerDataSource(see),
                new DiscardingSink<>(),
                new PrintSinkFunction<>(),
                machineDataCollectSink);

        ingestPipeline.build(see);
        ingestPipeline.execute(see);

        List<TelemetryDto> values = machineDataCollectSink.getValues();
        final Integer collect = values.stream()
                .map(s -> s.getTelemetryRecords().size())
//                .peek(System.out::println)
                .collect(Collectors.summingInt(Integer::intValue));
        assertThat(values.size(), Matchers.is(147));
        assertThat(collect, Matchers.is(2123));
    }
}
