package com.cnhindustrial.controller.emulator.rest;

import com.cnhindustrial.telemetry.common.model.TelemetryValidationRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.BlockingQueue;

import static com.cnhindustrial.telemetry.common.model.Constants.*;

@Component
@Scope("prototype")
public class ControllerMessageProducer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ControllerMessageProducer.class);
    private static final Random random = new Random();

    private final BlockingQueue<String> queue;
    private final ControllerMessageService messageService;
    private final ControllerMessageBuilder controllerMessageBuilder;

    ControllerMessageProducer(BlockingQueue<String> queue,
                              ControllerMessageService messageService,
                              ControllerMessageBuilder controllerMessageBuilder) {
        this.queue = queue;
        this.messageService = messageService;
        this.controllerMessageBuilder = controllerMessageBuilder;
    }

    @Override
    public void run() {
        try {
            Map<String, Object> inputParams = new HashMap<>();

            while (true) {
                int currentValue = messageService.getAndIncrementCounter();
                if (currentValue < messageService.getMaxNumberOfMessages()) {

                    // TODO set parameters we need for controller data
                    inputParams.put("assetId", getAssetId());
                    inputParams.put("lon", getLongitude());
                    inputParams.put("lat", getLatitude());
                    inputParams.put("time", getTime());

                    // TODO change to smaller value possibly
                    if (currentValue != 0 && currentValue % 100 == 0) {
                        setInvalidParam(inputParams);
                    }

                    queue.offer(produce(inputParams));
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException ex) {
            logger.error("Failed to send message to queue");
        }
    }

    private String produce(Map<String, Object> inputParams) {
        return controllerMessageBuilder.build(inputParams);
    }

    private String getAssetId() {
        return ASSET_IDS[random.nextInt(ASSET_IDS.length)];
    }

    private double getLongitude() {
        return MIN_LONGITUDE + random.nextDouble() * (MAX_LONGITUDE - MIN_LONGITUDE);
    }

    private double getLatitude() {
        return MIN_LATITUDE + random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE);
    }

    private String getTime() {
        return LocalDateTime.now().minusSeconds(1).format(DATE_TIME_FORMATTER);
    }

    private void setInvalidParam(Map<String, Object> inputParams) {
        switch (TelemetryValidationRules.values()[random.nextInt(TelemetryValidationRules.values().length)]) {
            case TIMESTAMP_AFTER:
                inputParams.put("time", LocalDateTime.now().plusMonths(1).format(DATE_TIME_FORMATTER));
                break;
            case TIMESTAMP_BEFORE:
                inputParams.put("time", LocalDateTime.now().minusYears(25).format(DATE_TIME_FORMATTER));
                break;
            case LATITUDE_LESS_THAN_MIN:
                inputParams.put("lat", MIN_LATITUDE - 1);
                break;
            case LATITUDE_BIGGER_THAN_MAX:
                inputParams.put("lat", MAX_LATITUDE + 1);
                break;
            case LONGITUDE_LESS_THAN_MIN:
                inputParams.put("lon", MIN_LONGITUDE - 1);
                break;
            case LONGITUDE_BIGGER_THAN_MAX:
                inputParams.put("lon", MAX_LONGITUDE + 1);
                break;
            case INVALID_VEHICLE_ID:
                inputParams.put("assetId", "INVALID_ASSET_ID");
                break;
            case INVALID_TIME_FORMAT:
//                inputParams.put("time", LocalDateTime.now().minusSeconds(1).format(ISO_LOCAL_DATE));
                break;
        }
    }
}
