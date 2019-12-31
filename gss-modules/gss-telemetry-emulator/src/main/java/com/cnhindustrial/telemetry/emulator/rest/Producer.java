package com.cnhindustrial.telemetry.emulator.rest;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Value("${cnh.gss.topic.name:users}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, TelemetryDto> kafkaTemplate;

    public void sendMessage(TelemetryDto message) {
        //logger.info("#### -> Producing message -> {}", message);
        this.kafkaTemplate.send(topic, message);
    }
}
