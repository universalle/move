package com.cnhindustrial.telemetry.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TelemetryDto implements Serializable {

    private static final long serialVersionUID = 5951886797659910769L;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TelemetryDto that = (TelemetryDto) o;
        return value == that.value &&
                Objects.equals(vehicleId, that.vehicleId) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, date, value);
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
