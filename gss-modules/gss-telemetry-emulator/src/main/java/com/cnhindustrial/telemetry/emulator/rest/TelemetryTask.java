package com.cnhindustrial.telemetry.emulator.rest;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
@Scope("prototype")
public class TelemetryTask implements Callable<TelemetryTask> {

    private TelemetryDto dto;

    @Autowired
    private Producer producer;

    public TelemetryTask(final TelemetryDto dto) {
        this.dto = dto;
    }

    @Override
    public TelemetryTask call() {
        producer.sendMessage(dto);
        return this;
    }
}
