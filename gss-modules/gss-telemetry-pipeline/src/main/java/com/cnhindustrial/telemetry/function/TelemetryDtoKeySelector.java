package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import org.apache.flink.api.java.functions.KeySelector;

public class TelemetryDtoKeySelector implements KeySelector<TelemetryDto, Long> {

    private static final long serialVersionUID = -2085285663032826318L;

    @Override
    public Long getKey(TelemetryDto value) throws Exception {
        return value.getDeviceid();
    }
}
