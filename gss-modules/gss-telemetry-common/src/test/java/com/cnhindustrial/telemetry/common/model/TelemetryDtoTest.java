package com.cnhindustrial.telemetry.common.model;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class TelemetryDtoTest {

    @Test
    void testSerializable() {
        ArrayList<TelemetryDto> array = new ArrayList<>();
        array.add(new TelemetryDto());
        ArrayList<TelemetryDto> deserialize = SerializationUtils.deserialize(SerializationUtils.serialize(array));

        assertIterableEquals(deserialize, array);
    }
}