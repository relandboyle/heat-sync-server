package com.ubibot.temperaturedata.model.ubibot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LastValues {

    @JsonProperty("log")
    private String log;

    @JsonProperty("field1")
    private String field1;

    @JsonProperty("name")
    private String name;

    @JsonProperty("last_values")
    private String lastValues;

    @JsonProperty("temperature")
    private String temperatureValue;
}
