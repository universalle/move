package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.common.model.TelemetryRecord;

import org.apache.flink.api.common.functions.JoinFunction;

import java.util.ArrayList;

public class TelemetryJoinFunction implements JoinFunction<TelemetryDto, TelemetryRecord, TelemetryDto> {

    private static final long serialVersionUID = -4543046525318788887L;

    @Override
    public TelemetryDto join(TelemetryDto first, TelemetryRecord second) throws Exception {
        if (first.getTelemetryRecords() == null) {
            first.setTelemetryRecords(new ArrayList<>());
        }
        first.getTelemetryRecords().add(second);
        return first;
    }
}
