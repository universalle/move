package com.cnhindustrial.telemetry.emulator.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class EmulatorController {

    @PostMapping("api/v1/emulate")
    @ApiOperation(value = "Emulate telemetry data and send it somewhere")
    public void emulate(int number) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
