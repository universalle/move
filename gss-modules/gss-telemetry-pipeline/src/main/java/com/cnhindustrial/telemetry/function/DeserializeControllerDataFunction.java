package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.json.BaseDeserializationSchema;
import com.cnhindustrial.telemetry.common.model.ControllerDto;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

public class DeserializeControllerDataFunction extends RichMapFunction<byte[], ControllerDto> {

    private static final long serialVersionUID = 7543482211529843854L;

    private BaseDeserializationSchema<ControllerDto> deserializationSchema;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        deserializationSchema = new BaseDeserializationSchema<>(ControllerDto.class);
    }

    @Override
    public ControllerDto map(byte[] value) throws Exception {
        return deserializationSchema.deserialize(value);
    }
}
