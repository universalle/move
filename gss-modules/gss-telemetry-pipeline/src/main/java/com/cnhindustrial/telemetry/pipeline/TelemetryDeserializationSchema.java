package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.json.BaseDeserializationSchema;

public class TelemetryDeserializationSchema extends BaseDeserializationSchema<String> {
    public TelemetryDeserializationSchema() {
        super(String.class);
    }
}
