package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.json.BaseDeserializationSchema;
import com.cnhindustrial.telemetry.common.model.TelemetryDto;

public class TelemetryDeserilazationSchema extends BaseDeserializationSchema<TelemetryDto> {
    public TelemetryDeserilazationSchema() {
        super(TelemetryDto.class);
    }
}
