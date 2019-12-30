package com.cnhindustrial.telemetry.common.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;

import java.io.IOException;

public class BaseDeserializationSchema<T> extends AbstractDeserializationSchema<T> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Class<? extends T> valueType;

    public BaseDeserializationSchema(Class<T> valueType) {
        this.valueType = valueType;
    }

    public T deserialize(byte[] source) throws IOException {
        return OBJECT_MAPPER.readValue(source, valueType);
    }
}
