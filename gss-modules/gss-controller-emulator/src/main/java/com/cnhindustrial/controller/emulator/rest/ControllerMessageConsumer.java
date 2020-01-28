package com.cnhindustrial.controller.emulator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
@Scope("prototype")
public class ControllerMessageConsumer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ControllerMessageConsumer.class);
    private final BlockingQueue<String> queue;
    private final KafkaProducer kafkaProducer;

    ControllerMessageConsumer(BlockingQueue<String> queue, KafkaProducer kafkaProducer) {
        this.queue = queue;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                consume(queue.take());
            } catch (InterruptedException e) {
                logger.error("Failed to consume message from queue");
            }
        }
    }

    void consume(String message) {
        kafkaProducer.sendMessage(message);
    }
}
