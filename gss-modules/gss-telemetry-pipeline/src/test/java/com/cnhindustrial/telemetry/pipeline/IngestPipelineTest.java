package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.test.MachineDataCollectSink;
import com.cnhindustrial.telemetry.test.MiniClusterWithClientResourceExtension;

import org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.FromElementsFunction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@ExtendWith(MiniClusterWithClientResourceExtension.class)
class IngestPipelineTest {

    private StreamExecutionEnvironment see;
    private MachineDataCollectSink machineDataCollectSink;

    @BeforeEach
    void setUp() {
        see = StreamExecutionEnvironment.getExecutionEnvironment();

        // configure your test environment
        see.setParallelism(2);

        machineDataCollectSink = new MachineDataCollectSink();
    }

    @AfterEach
    void tearDown() {
        machineDataCollectSink.clear();
    }

    @Test
    void testThreeTelemetryItemsInPipeline() throws Exception {
        ArrayList<TelemetryDto> inputArray = new ArrayList<>();
        inputArray.add(createTelemetryDto("1"));
        inputArray.add(createTelemetryDto("2"));
        inputArray.add(createTelemetryDto("3"));


        IngestPipeline ingestPipeline = new IngestPipeline(
                new FromElementsFunction<>(new KryoSerializer<>(TelemetryDto.class, see.getConfig()), inputArray),
                null,
                machineDataCollectSink,
                null);

        ingestPipeline.build(see);
        ingestPipeline.execute(see);

        assertThat(machineDataCollectSink.getValues(), containsInAnyOrder(
                "TelemetryDto{vehicleId='1', date=null, value=0}",
                "TelemetryDto{vehicleId='2', date=null, value=0}",
                "TelemetryDto{vehicleId='3', date=null, value=0}"
        ));
    }

    private TelemetryDto createTelemetryDto(String vehicleId) {
        TelemetryDto result = new TelemetryDto();
        result.setVehicleId(vehicleId);
        return result;
    }
}