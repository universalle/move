package com.cnhindustrial.telemetry.emulator.rest;

import org.springframework.stereotype.Component;

@Component
public class TelemetryMessageBuilder extends MessageBuilder {

    private final String statusMessageTemplate = "{" +
            "\"messageType\":\"Status\"," +
            "\"pos\": {" +
            "  \"pdop\":-1," +
            "  \"current\":true," +
            "  \"satcount\":-1," +
            "  \"fixtype\":4," +
            "  \"alt\":64.475," +
            "  \"lon\":${lon}," +
            "  \"time\":\"${time}\"," +
            "  \"lat\":${lat}," +
            "  \"speed\":0," +
            "  \"direction\":118.78" +
            "}," +
            "\"status\":{" +
            "  \"duty\":11," +
            "  \"device\":\"on\"" +
            "}," +
            "\"networkinfo\":{" +
            "  \"rssi\":48," +
            "  \"mnc\":\"610\"," +
            "  \"networkStatus\":\"roaming\"," +
            "  \"connection\":\"UMTS\"," +
            "  \"mcc\":\"302\"," +
            "  \"operatorName\":\"DATA ONLY (Bell)\"" +
            "}," +
            "\"telemetryRecords\":[" +
            "   ${telemetryData}" +
            "]," +
            "\"reports\": null," +
            "\"events\": null," +
            "\"time\":\"${time}\"," +
            "\"deviceid\":\"81758916982\"," +
            "\"assetId\":\"${assetId}\"," +
            "\"from\":\"PCM_TELEMATICS\"," +
            "\"to\":\"SDP\"," +
            "\"techType\":\"696150782\"" +
            "}";

    @Override
    public String getTemplate() {
        return statusMessageTemplate;
    }

    @Override
    public String getTemplateName() {
        return "status";
    }
}
