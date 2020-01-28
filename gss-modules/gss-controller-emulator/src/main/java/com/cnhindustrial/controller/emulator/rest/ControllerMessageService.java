package com.cnhindustrial.controller.emulator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ControllerMessageService {
    private static final Logger logger = LoggerFactory.getLogger(ControllerMessageService.class);

    private int maxNumberOfMessages = 0;
    private AtomicInteger counter = new AtomicInteger(0);

    ControllerMessageService() {}

    public int getMaxNumberOfMessages() {
        return maxNumberOfMessages;
    }

    public void setMaxNumberOfMessages(int maxNumberOfMessages) {
        logger.info("Setting max number of messages per half an hour to {}.", maxNumberOfMessages);
        this.maxNumberOfMessages = maxNumberOfMessages;
    }

    public int getAndIncrementCounter() {
        return counter.getAndUpdate(value -> value > maxNumberOfMessages ? value : ++value);
    }

    @Scheduled(fixedRate = 1_800_000)
    public void resetCounter() {
        counter.set(0);
    }
}
