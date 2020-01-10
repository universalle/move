package com.cnhindustrial.telemetry.common.model;

public class Status {
    private int duty;
    private String device;

    public int getDuty() {
        return duty;
    }

    public void setDuty(int duty) {
        this.duty = duty;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "Status{" +
                "duty=" + duty +
                ", device='" + device + '\'' +
                '}';
    }
}
