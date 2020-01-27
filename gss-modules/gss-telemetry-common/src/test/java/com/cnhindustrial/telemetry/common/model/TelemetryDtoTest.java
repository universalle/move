package com.cnhindustrial.telemetry.common.model;

import com.cnhindustrial.telemetry.common.json.BaseDeserializationSchema;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TelemetryDtoTest {

    public static final String MESSAGE1 = "{\"messageType\":\"Status\",\"pos\": {  \"pdop\":-1,  \"current\":true,  \"satcount\":-1,  \"fixtype\":4,  \"alt\":64.475,  \"lon\":-105.868,  \"time\":\"2020-01-14T10:41:25Z\",  \"lat\":37.148,  \"speed\":0,  \"direction\":118.78},\"status\":{  \"duty\":11,  \"device\":\"on\"},\"networkinfo\":{  \"rssi\":48,  \"MNC\":\"610\",  \"networkStatus\":\"roaming\",  \"connection\":\"UMTS\",  \"mcc\":\"302\",  \"operatorName\":\"DATA ONLY (Bell)\"},\"telemetryRecords\":[   {  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"AVG_FUEL_DISTANCE\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"AREA\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"AREA_REMAIN\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"WORK_RATE_AVG\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"ENG_SPEED\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"ENG_LOAD\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"DEF_RATE\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"ENG_HOURS\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"DRIVELINE_HOURS\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"ENG_FUEL_RATE\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"ENG_COOLANT_TEMP\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"BATTERY_VOLTAGE\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"ENG_OIL_TEMP\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"INTAKE_TEMP\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"DEF_LEVEL\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"REAR_PTO_SPEED\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"GROUND_SPEED\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"AIR_BRAKE_PRESS\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"ENG_BOOST_PRESS\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"FNT_PTO_SPEED\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"REAR_HITCH_POS\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"TRANS_OIL_PRESS\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"HYD_OIL_TEMP\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"SLIP\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"TRANS_RANGE\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"TRANS_STATUS\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"TRANS_LUBE_PRESS\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"GEAR_SELECTED\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"TRANS_OIL_TEMP\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"FUEL_LEVEL\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239},{  \"messageType\":\"Telemetry\",  \"id\": 757,  \"offset\": 0,  \"resolution\": 1,  \"familyCode\": \"ENG_OIL_PRESS\",  \"parameterType\": \"signal\",  \"reportType\": \"Last\",  \"samplingPeriod\": 0,  \"issigned\": false,  \"confidential\": true,  \"valueType\": \"Numeric\",  \"last\": 81424.239,  \"translatedValue\": 81424.239}],\"reports\": null,\"events\": null,\"time\":\"2020-01-14T10:41:25Z\",\"deviceid\":\"81758916982\",\"assetId\":\"DL24XZQPFEV96ZUYQ\",\"from\":\"PCM_TELEMATICS\",\"to\":\"SDP\",\"techType\":\"696150782\"}";

    @Test
    void testSerializable() {
        ArrayList<TelemetryDto> array = new ArrayList<>();
        array.add(new TelemetryDto());
        ArrayList<TelemetryDto> deserialize = SerializationUtils.deserialize(SerializationUtils.serialize(array));

        assertEquals(deserialize.get(0), array.get(0));
    }

    @Test
    void test() throws IOException {
        BaseDeserializationSchema<TelemetryDto> deserializationSchema = new BaseDeserializationSchema<>(TelemetryDto.class);
        TelemetryDto dto = deserializationSchema.deserialize(MESSAGE1.getBytes());
    }
}