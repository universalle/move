package com.cnhindustrial.telemetry.common.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Autogenerated code Controller Data transfer object. To be reviewed.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "@Id",
    "Grower",
    "Persons",
    "EventDate",
    "EventEndDate",
    "Notes",
    "DeviceElements",
    "DeviceElementConfigurations",
    "SummaryData",
    "OperationSummaries"
})
public class Summary implements Serializable
{

    @JsonProperty("@Id")
    private String id;
    @JsonProperty("Grower")
    private Grower grower;
    @JsonProperty("Persons")
    private List<Object> persons = null;
    @JsonProperty("EventDate")
    private String eventDate;
    @JsonProperty("EventEndDate")
    private String eventEndDate;
    @JsonProperty("Notes")
    private List<Object> notes = null;
    @JsonProperty("DeviceElements")
    private List<Object> deviceElements = null;
    @JsonProperty("DeviceElementConfigurations")
    private List<Object> deviceElementConfigurations = null;
    @JsonProperty("SummaryData")
    private List<Object> summaryData = null;
    @JsonProperty("OperationSummaries")
    private List<Object> operationSummaries = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
    private final static long serialVersionUID = 6553868928260506484L;

    @JsonProperty("@Id")
    public String getId() {
        return id;
    }

    @JsonProperty("@Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Grower")
    public Grower getGrower() {
        return grower;
    }

    @JsonProperty("Grower")
    public void setGrower(Grower grower) {
        this.grower = grower;
    }

    @JsonProperty("Persons")
    public List<Object> getPersons() {
        return persons;
    }

    @JsonProperty("Persons")
    public void setPersons(List<Object> persons) {
        this.persons = persons;
    }

    @JsonProperty("EventDate")
    public String getEventDate() {
        return eventDate;
    }

    @JsonProperty("EventDate")
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    @JsonProperty("EventEndDate")
    public String getEventEndDate() {
        return eventEndDate;
    }

    @JsonProperty("EventEndDate")
    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    @JsonProperty("Notes")
    public List<Object> getNotes() {
        return notes;
    }

    @JsonProperty("Notes")
    public void setNotes(List<Object> notes) {
        this.notes = notes;
    }

    @JsonProperty("DeviceElements")
    public List<Object> getDeviceElements() {
        return deviceElements;
    }

    @JsonProperty("DeviceElements")
    public void setDeviceElements(List<Object> deviceElements) {
        this.deviceElements = deviceElements;
    }

    @JsonProperty("DeviceElementConfigurations")
    public List<Object> getDeviceElementConfigurations() {
        return deviceElementConfigurations;
    }

    @JsonProperty("DeviceElementConfigurations")
    public void setDeviceElementConfigurations(List<Object> deviceElementConfigurations) {
        this.deviceElementConfigurations = deviceElementConfigurations;
    }

    @JsonProperty("SummaryData")
    public List<Object> getSummaryData() {
        return summaryData;
    }

    @JsonProperty("SummaryData")
    public void setSummaryData(List<Object> summaryData) {
        this.summaryData = summaryData;
    }

    @JsonProperty("OperationSummaries")
    public List<Object> getOperationSummaries() {
        return operationSummaries;
    }

    @JsonProperty("OperationSummaries")
    public void setOperationSummaries(List<Object> operationSummaries) {
        this.operationSummaries = operationSummaries;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(persons).append(eventEndDate).append(notes).append(operationSummaries).append(deviceElementConfigurations).append(deviceElements).append(id).append(additionalProperties).append(grower).append(summaryData).append(eventDate).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Summary)) {
            return false;
        }
        Summary rhs = ((Summary) other);
        return new EqualsBuilder().append(persons, rhs.persons).append(eventEndDate, rhs.eventEndDate).append(notes, rhs.notes).append(operationSummaries, rhs.operationSummaries).append(deviceElementConfigurations, rhs.deviceElementConfigurations).append(deviceElements, rhs.deviceElements).append(id, rhs.id).append(additionalProperties, rhs.additionalProperties).append(grower, rhs.grower).append(summaryData, rhs.summaryData).append(eventDate, rhs.eventDate).isEquals();
    }

}
