package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.model.TelemetryMessage;
import com.cnhindustrial.telemetry.common.model.TelemetryRecord;
import com.cnhindustrial.telemetry.function.StatusKeySelector.TelemetryKey;

import org.apache.flink.api.java.functions.KeySelector;

public class TelemetryKeySelector2 implements KeySelector<TelemetryMessage, TelemetryKey> {

    private static final long serialVersionUID = 6981903983304465637L;

    @Override
    public TelemetryKey getKey(TelemetryMessage telemetryRecord) {
        return new TelemetryKey(telemetryRecord.getDeviceId(), telemetryRecord.getAssetId());
    }
}
