package com.cnhindustrial.telemetry.common.model;

import java.util.Date;

public class TelemetryDto {

    private String vehicleId;
    private Date date;
    private int value;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TelemetryDto{" +
                "vehicleId='" + vehicleId + '\'' +
                ", date=" + date +
                ", value=" + value +
                '}';
    }
}
