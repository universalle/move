package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.json.BaseDeserializationSchema;
import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import org.apache.flink.api.common.functions.MapFunction;

public class DeserializeTelemetryDataFunction implements MapFunction<String, TelemetryDto> {

    private static final long serialVersionUID = 970911997909031379L;

    private BaseDeserializationSchema<TelemetryDto> deserializationSchema = new BaseDeserializationSchema<>(TelemetryDto.class);;

    @Override
    public TelemetryDto map(String s) throws Exception {
        return deserializationSchema.deserialize(s);
    }
}
