package com.cnhindustrial.controller.emulator.rest;

import org.springframework.stereotype.Component;

@Component
public class ControllerMessageBuilder extends MessageBuilder {

    // TODO add/load message template
    private static final String CONTROLLER_MESSAGE_TEMPLATE = "";

    @Override
    public String getTemplate() {
        return CONTROLLER_MESSAGE_TEMPLATE;
    }

    @Override
    public String getTemplateName() {
        return "controller";
    }
}
