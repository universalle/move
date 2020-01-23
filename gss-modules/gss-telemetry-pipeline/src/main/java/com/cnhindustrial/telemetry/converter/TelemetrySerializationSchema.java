package com.cnhindustrial.telemetry.converter;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;

public class TelemetrySerializationSchema implements KafkaSerializationSchema<TelemetryDto> {

    private String topic;

    public TelemetrySerializationSchema(String topic) {
        super();
        this.topic = topic;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(TelemetryDto telemetryDto, @Nullable Long aLong) {
        return new ProducerRecord<>(topic, SerializationUtils.serialize(telemetryDto));
    }
}
