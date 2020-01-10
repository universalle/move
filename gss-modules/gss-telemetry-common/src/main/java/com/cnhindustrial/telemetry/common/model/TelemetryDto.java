package com.cnhindustrial.telemetry.common.model;

import java.io.Serializable;
import java.util.List;

public class TelemetryDto implements Serializable {

    private static final long serialVersionUID = 5951886797659910769L;

    private String messageType;
    private String events;
    private String time;
    private Long deviceid;
    private String assetId;
    private String from;
    private String to;
    private Long techType;
    private String reports;

    private Position pos;
    private Status status;
    private NetworkInfo networkinfo;
    private List<TelemetryRecord> telemetryRecords;

    public TelemetryDto() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Long deviceid) {
        this.deviceid = deviceid;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Long getTechType() {
        return techType;
    }

    public void setTechType(Long techType) {
        this.techType = techType;
    }

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public NetworkInfo getNetworkinfo() {
        return networkinfo;
    }

    public void setNetworkinfo(NetworkInfo networkinfo) {
        this.networkinfo = networkinfo;
    }

    public List<TelemetryRecord> getTelemetryRecords() {
        return telemetryRecords;
    }

    public void setTelemetryRecords(List<TelemetryRecord> telemetryRecords) {
        this.telemetryRecords = telemetryRecords;
    }

    @Override
    public String toString() {
        return "TelemetryDto{" +
                "messageType='" + messageType + '\'' +
                ", events='" + events + '\'' +
                ", time='" + time + '\'' +
                ", deviceid=" + deviceid +
                ", assetId='" + assetId + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", techType=" + techType +
                ", reports='" + reports + '\'' +
                ", pos=" + pos +
                ", status=" + status +
                ", networkinfo=" + networkinfo +
                ", telemetryRecords=" + telemetryRecords +
                '}';
    }
}
