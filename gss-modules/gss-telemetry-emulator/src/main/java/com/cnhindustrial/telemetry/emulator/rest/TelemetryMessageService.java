package com.cnhindustrial.telemetry.emulator.rest;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TelemetryMessageService {

    private static final Logger logger = LoggerFactory.getLogger(EmulatorController.class);
    public static final Random RANDOM = new Random();

    private final BeanFactory beanFactory;

    public TelemetryMessageService(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public List<TelemetryDto> generateTelemetryDtoList(int number) {
        return IntStream
                .range(0, number)
                .mapToObj(id -> createTelemetryDto("vehicle_id_" + id))
                .collect(Collectors.toList());
    }

    public List<Callable<TelemetryTask>> createTelemetryTasks(List<TelemetryDto> telemetryDtoList) {
        return telemetryDtoList
                .parallelStream()
                .map(dto -> beanFactory.getBean(TelemetryTask.class, dto))
                .collect(Collectors.toList());
    }

    public void executeTelemetryTasks(List<Callable<TelemetryTask>> tasks) {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        try {
            executor.invokeAll(tasks, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Failed to execute telemetry tasks", e);
        }
    }

    public TelemetryDto createTelemetryDto(String id) {
        final TelemetryDto telemetryDto = new TelemetryDto();

        telemetryDto.setVehicleId(id);
        telemetryDto.setDate(new Date());
        telemetryDto.setValue(RANDOM.nextInt());

        return telemetryDto;
    }
}
