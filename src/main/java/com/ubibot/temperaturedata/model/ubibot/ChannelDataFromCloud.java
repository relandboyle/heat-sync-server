package com.ubibot.temperaturedata.model.ubibot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelDataFromCloud {

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("field1")
    private String fieldOneLabel;

    @JsonProperty("name")
    private String name;

    @JsonProperty("last_values")
    private String lastValues;

    @JsonProperty("temperature")
    private String temperatureValue;

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("longitude")
    private String longitude;
}
