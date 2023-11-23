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
//    @JsonValue
//    private String latitude;
//    @JsonValue
//    private String longitude;
    @JsonProperty("name")
    private String name;
//    @JsonValue
//    private String metadata;
//    @JsonValue
//    private String created_at;
//    @JsonValue
//    private String updated_at;
//    @JsonValue
//    private String attached_at;
    @JsonProperty("last_values")
    private String lastValues;
}
