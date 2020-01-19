package com.cnhindustrial.telemetry.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public interface TelemetryMessage {
    abstract Date getTime();

    Long getDeviceId();

    String getAssetId();
}
