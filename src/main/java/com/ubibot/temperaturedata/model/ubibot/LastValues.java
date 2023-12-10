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

    @JsonProperty("field2")
    private String field2;

    @JsonProperty("field3")
    private String field3;

    @JsonProperty("field4")
    private String field4;

    @JsonProperty("field5")
    private String field5;
}
