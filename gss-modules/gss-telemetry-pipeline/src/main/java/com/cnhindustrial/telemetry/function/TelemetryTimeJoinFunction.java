package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.common.model.TelemetryRecord;
import com.cnhindustrial.telemetry.function.StatusKeySelector.TelemetryKey;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map.Entry;

public class TelemetryTimeJoinFunction extends KeyedCoProcessFunction<TelemetryKey, TelemetryDto, TelemetryRecord, TelemetryDto> {

    private static final long serialVersionUID = -1200885888116050520L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TelemetryTimeJoinFunction.class);

    private ValueState<Long> statusLatest = null;

    // Store pending Trades for a customerId, keyed by timestamp
    private MapState<Long, TelemetryDto> statusMap = null;

    // Store Customer updates for a customerId, keyed by timestamp
    private ListState<TelemetryRecord> telemetryList = null;

    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<Long> lDescriptor = new ValueStateDescriptor("statusLatest", Long.class);
        statusLatest = getRuntimeContext().getState(lDescriptor);

        MapStateDescriptor sDescriptor = new MapStateDescriptor<>(
                "statusBuffer",
                TypeInformation.of(Long.class),
                TypeInformation.of(TelemetryDto.class)
        );
        statusMap = getRuntimeContext().getMapState(sDescriptor);

        ListStateDescriptor tDescriptor = new ListStateDescriptor<>(
                "telemetryBuffer",
                TypeInformation.of(TelemetryRecord.class)
        );
        telemetryList = getRuntimeContext().getListState(tDescriptor);
    }

    @Override
    public void processElement1(TelemetryDto statusDto,
                                Context context,
                                Collector<TelemetryDto> collector) throws Exception {
//        LOGGER.debug("Received status {}", statusDto);
        TimerService timerService = context.timerService();

        if (context.timestamp() > timerService.currentWatermark()) {
            telemetryList.get().forEach(statusDto.getTelemetryRecords()::add);
            telemetryList.clear();
            long fireTime = statusDto.getTime().getTime() + 1500;
            statusMap.put(fireTime, statusDto);
            timerService.registerEventTimeTimer(fireTime);
            statusLatest.update(fireTime);
        } else {
            LOGGER.debug("Late status: {}", statusDto);
        }
    }

    @Override
    public void processElement2(TelemetryRecord telemetryRecord,
                                Context context,
                                Collector<TelemetryDto> collector) throws Exception {
//        LOGGER.debug("Received telemetry {}", telemetryRecord);
        Long latest = statusLatest.value();
        if (latest != null) {
            TelemetryDto telemetryDto = statusMap.get(latest);
            telemetryDto.getTelemetryRecords().add(telemetryRecord);
            statusMap.put(latest, telemetryDto);
        } else {
            telemetryList.add(telemetryRecord);
        }
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<TelemetryDto> out) throws Exception {
        LOGGER.debug("On timer {}", timestamp);
        TelemetryDto statusDto = statusMap.get(timestamp);
        if (statusDto != null) {
            statusMap.remove(timestamp);
//            telemetryList.get().forEach(statusDto.getTelemetryRecords()::add);
            out.collect(statusDto);
//            telemetryList.clear();
        }
    }
}
