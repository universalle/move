package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.model.TelemetryRecord;
import com.cnhindustrial.telemetry.function.StatusKeySelector.TelemetryKey;

import org.apache.flink.api.java.functions.KeySelector;

public class TelemetryKeySelector implements KeySelector<TelemetryRecord, StatusKeySelector.TelemetryKey> {

    private static final long serialVersionUID = 6981903983304465637L;

    @Override
    public TelemetryKey getKey(TelemetryRecord TelemetryRecord) {
        return new TelemetryKey(TelemetryRecord.getDeviceId(), TelemetryRecord.getAssetId());
    }
}
