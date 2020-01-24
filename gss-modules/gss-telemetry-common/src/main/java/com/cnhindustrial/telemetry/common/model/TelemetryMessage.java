package com.cnhindustrial.telemetry.common.model;

import java.util.Date;

public interface TelemetryMessage {
    abstract Date getTime();

    Long getDeviceId();

    String getAssetId();
}
