package com.cnhindustrial.telemetry.emulator.rest;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping(value = "/api/kafka")
public class KafkaController {

    private final Producer producer;

    public static final Random RANDOM = new Random();

    @Autowired
    KafkaController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {

        final TelemetryDto telemetryDto = new TelemetryDto();

        telemetryDto.setVehicleId(message);
        telemetryDto.setDate(new Date());
        telemetryDto.setValue(RANDOM.nextInt());

        this.producer.sendMessage(telemetryDto);
    }
}
