package com.cnhindustrial.telemetry.emulator.rest;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
@Scope("prototype")
public class TelemetryMessageProducer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TelemetryMessageProducer.class);

    private final BlockingQueue<TelemetryDto> queue;
    private final TelemetryMessageService messageService;

    TelemetryMessageProducer(BlockingQueue<TelemetryDto> queue, TelemetryMessageService messageService) {
        this.queue = queue;
        this.messageService = messageService;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (messageService.getAndIncrementCounter() < messageService.getMaxNumberOfMessages()) {
                    queue.offer(produce());
                } else {
                    Thread.sleep(50);
                }
            }
        } catch (InterruptedException ex) {
            logger.error("Failed to send message to queue");
        }
    }

    TelemetryDto produce() {
        return messageService.createTelemetryDto();
    }
}
