package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.common.model.TelemetryRecord;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;

public class TelemetryCoFlatMapFunction extends RichCoFlatMapFunction<TelemetryDto, TelemetryRecord, TelemetryDto> {

    private static final long serialVersionUID = 7512943726379752598L;

    private ValueState<TelemetryDto> statusState;
    private ListState<TelemetryRecord> telemetryState;

    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<TelemetryDto> statusDescriptor = new ValueStateDescriptor<>("status-cache", TelemetryDto.class);
        ListStateDescriptor<TelemetryRecord> telemetryDescriptor = new ListStateDescriptor<>("record-cache", TelemetryRecord.class);

        statusState = getRuntimeContext().getState(statusDescriptor);
        telemetryState = getRuntimeContext().getListState(telemetryDescriptor);
    }

    @Override
    public void flatMap1(TelemetryDto value, Collector<TelemetryDto> out) throws Exception {
        TelemetryDto previousStatus = statusState.value();
        if (previousStatus != null) {
            Iterable<TelemetryRecord> iterable = telemetryState.get();
            if (iterable != null) {
                for (TelemetryRecord telemetryRecord : iterable) {
                    value.getTelemetryRecords().add(telemetryRecord);
                }
            }
            telemetryState.clear();
            out.collect(previousStatus);
        } else {

        }
        statusState.update(value);
    }

    @Override
    public void flatMap2(TelemetryRecord value, Collector<TelemetryDto> out) throws Exception {
        TelemetryDto status = statusState.value();
        if (status != null) {
            status.getTelemetryRecords().add(value);
            statusState.update(status);
        } else {
            telemetryState.add(value);
        }
    }
}
