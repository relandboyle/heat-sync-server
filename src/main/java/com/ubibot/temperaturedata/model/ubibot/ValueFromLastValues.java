package com.ubibot.temperaturedata.model.ubibot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValueFromLastValues {

    @JsonProperty("value")
    private Map<String, Object> value;

    @JsonProperty("created_at")
    private Map<String, String> createdAt;
}
