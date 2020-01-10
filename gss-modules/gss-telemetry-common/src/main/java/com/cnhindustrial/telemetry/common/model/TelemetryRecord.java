package com.cnhindustrial.telemetry.common.model;

import java.io.Serializable;

public class TelemetryRecord implements Serializable {
    private int id;
    private int offset;
    private int resolution;
    private String familyCode;
    private String parameterType;
    private String reportType;
    private int samplingPeriod;
    private boolean issigned;
    private boolean confidential;
    private String valueType;
    private double last;
    private double translatedValue;

    public TelemetryRecord() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public String getFamilyCode() {
        return familyCode;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public int getSamplingPeriod() {
        return samplingPeriod;
    }

    public void setSamplingPeriod(int samplingPeriod) {
        this.samplingPeriod = samplingPeriod;
    }

    public boolean isIssigned() {
        return issigned;
    }

    public void setIssigned(boolean issigned) {
        this.issigned = issigned;
    }

    public boolean isConfidential() {
        return confidential;
    }

    public void setConfidential(boolean confidential) {
        this.confidential = confidential;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public double getTranslatedValue() {
        return translatedValue;
    }

    public void setTranslatedValue(double translatedValue) {
        this.translatedValue = translatedValue;
    }

    @Override
    public String toString() {
        return "TelemetryRecord{" +
                "id=" + id +
                ", offset=" + offset +
                ", resolution=" + resolution +
                ", familyCode='" + familyCode + '\'' +
                ", parameterType='" + parameterType + '\'' +
                ", reportType='" + reportType + '\'' +
                ", samplingPeriod=" + samplingPeriod +
                ", issigned=" + issigned +
                ", confidential=" + confidential +
                ", valueType='" + valueType + '\'' +
                ", last=" + last +
                ", translatedValue=" + translatedValue +
                '}';
    }
}
