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
    "OperationDatas"
})
public class LoggedData implements Serializable {

    private final static long serialVersionUID = -3756099470338943403L;

    @JsonProperty("OperationDatas")
    private List<Object> operationDatas = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("OperationDatas")
    public List<Object> getOperationDatas() {
        return operationDatas;
    }

    @JsonProperty("OperationDatas")
    public void setOperationDatas(List<Object> operationDatas) {
        this.operationDatas = operationDatas;
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
        return new HashCodeBuilder().append(operationDatas).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LoggedData)) {
            return false;
        }
        LoggedData rhs = ((LoggedData) other);
        return new EqualsBuilder().append(operationDatas, rhs.operationDatas).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
