package com.cnhindustrial.telemetry.emulator.rest;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/kafka")
public class KafkaController {

    private final KafkaProducer kafkaProducer;
    private final TelemetryMessageService messageService;

    @Autowired
    KafkaController(KafkaProducer kafkaProducer, TelemetryMessageService messageService) {
        this.kafkaProducer = kafkaProducer;
        this.messageService = messageService;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        final TelemetryDto telemetryDto = messageService.createTelemetryDto(message);
        this.kafkaProducer.sendMessage(telemetryDto);
    }
}
