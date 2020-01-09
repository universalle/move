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
    "Name",
    "Farms"
})
public class Grower implements Serializable {

    private static final long serialVersionUID = -1090929431807367030L;

    @JsonProperty("@Id")
    private String id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Farms")
    private List<Farm> farms = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("@Id")
    public String getId() {
        return id;
    }

    @JsonProperty("@Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Farms")
    public List<Farm> getFarms() {
        return farms;
    }

    @JsonProperty("Farms")
    public void setFarms(List<Farm> farms) {
        this.farms = farms;
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
        return new HashCodeBuilder().append(name).append(id).append(additionalProperties).append(farms).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Grower)) {
            return false;
        }
        Grower rhs = ((Grower) other);
        return new EqualsBuilder().append(name, rhs.name).append(id, rhs.id).append(additionalProperties, rhs.additionalProperties).append(farms, rhs.farms).isEquals();
    }

}
