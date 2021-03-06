package com.cnhindustrial.telemetry.emulator.rest;

import com.cnhindustrial.telemetry.common.json.BaseDeserializationSchema;
import com.cnhindustrial.telemetry.common.model.TelemetryRecord;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Message generated by {@link TelemetryRecordMessageBuilder} is deserializable in the pipeline by {@link BaseDeserializationSchema}.
 */
class TelemetryRecordMessageBuilderTest {

    private MessageBuilder messageBuilder = new TelemetryRecordMessageBuilder();
    private BaseDeserializationSchema<TelemetryRecord> deserializationSchema = new BaseDeserializationSchema<>(TelemetryRecord.class);

    @Test
    void template() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("familyCode", "ENG_OIL_TEMP");

        String build = messageBuilder.build(params);
        TelemetryRecord deserialized = deserializationSchema.deserialize(build.getBytes());

        Assertions.assertEquals("ENG_OIL_TEMP", deserialized.getFamilyCode());
    }

    @Test
    void name() {
        Assertions.assertEquals("telemetry", messageBuilder.getTemplateName());
    }
}