package com.cnhindustrial.telemetry.common.model;

public class Position {
    private int pdop;
    private boolean current;
    private int satcount;
    private int fixtype;
    private float alt;
    private double lon;
    private String time;
    private double lat;
    private int speed;
    private float direction;

    public int getPdop() {
        return pdop;
    }

    public void setPdop(int pdop) {
        this.pdop = pdop;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public int getSatcount() {
        return satcount;
    }

    public void setSatcount(int satcount) {
        this.satcount = satcount;
    }

    public int getFixtype() {
        return fixtype;
    }

    public void setFixtype(int fixtype) {
        this.fixtype = fixtype;
    }

    public float getAlt() {
        return alt;
    }

    public void setAlt(float alt) {
        this.alt = alt;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Position{" +
                "pdop=" + pdop +
                ", current=" + current +
                ", satcount=" + satcount +
                ", fixtype=" + fixtype +
                ", alt=" + alt +
                ", lon=" + lon +
                ", time='" + time + '\'' +
                ", lat=" + lat +
                ", speed=" + speed +
                ", direction=" + direction +
                '}';
    }
}
