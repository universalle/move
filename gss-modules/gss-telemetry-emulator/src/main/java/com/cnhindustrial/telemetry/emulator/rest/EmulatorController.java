package com.cnhindustrial.telemetry.emulator.rest;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;

@RestController
public class EmulatorController {
    private static final Logger logger = LoggerFactory.getLogger(EmulatorController.class);

    private final TelemetryMessageService messageService;

    public EmulatorController(TelemetryMessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("api/v1/emulate")
    @ApiOperation(value = "Emulate telemetry data and send it somewhere")
    public void emulate(int number) {

        Instant start = Instant.now();
        List<TelemetryDto> telemetryDtoList = messageService.generateTelemetryDtoList(number);
        Instant end = Instant.now();
        logger.info("Time to generate messages: " + Duration.between(start, end).toMillis() + " ms");


        start = Instant.now();
        List<Callable<TelemetryTask>> tasks = messageService.createTelemetryTasks(telemetryDtoList);
        end = Instant.now();
        logger.info("Time to create callable tasks: " + Duration.between(start, end).toMillis() + " ms");

        start = Instant.now();
        messageService.executeTelemetryTasks(tasks);
        end = Instant.now();
        logger.info("Time to send to kafka: " + Duration.between(start, end).toMillis() + " ms");
    }
}
