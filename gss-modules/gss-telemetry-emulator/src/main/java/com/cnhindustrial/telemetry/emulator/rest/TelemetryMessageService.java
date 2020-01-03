package com.cnhindustrial.telemetry.emulator.rest;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TelemetryMessageService {
    private static final Logger logger = LoggerFactory.getLogger(TelemetryMessageService.class);
    public static final Random RANDOM = new Random();

    private int maxNumberOfMessages = 0;
    private AtomicInteger counter = new AtomicInteger(0);
    private final BlockingQueue<TelemetryDto> queue;

    TelemetryMessageService(BlockingQueue<TelemetryDto> queue) {
        this.queue = queue;
    }

    public int getMaxNumberOfMessages() {
        return maxNumberOfMessages;
    }

    public void setMaxNumberOfMessages(int maxNumberOfMessages) {
        this.maxNumberOfMessages = maxNumberOfMessages;
    }

    public int getAndIncrementCounter() {
        return counter.getAndUpdate(value -> value > maxNumberOfMessages ? value : ++value);
    }

    @Scheduled(fixedRate=1000)
    public void resetCounter() {
        logger.info("Messages in queue: " + queue.size());
        counter.set(0);
    }

    public TelemetryDto createTelemetryDto() {
        return createTelemetryDto("vehicleId" + counter.get());
    }

    public TelemetryDto createTelemetryDto(String message) {
        final TelemetryDto telemetryDto = new TelemetryDto();

        telemetryDto.setVehicleId(message);
        telemetryDto.setDate(new Date());
        telemetryDto.setValue(RANDOM.nextInt());

        return telemetryDto;
    }
}
