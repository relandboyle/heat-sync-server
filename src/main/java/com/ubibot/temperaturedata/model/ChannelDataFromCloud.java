package com.ubibot.temperaturedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelDataFromCloud {
    @JsonValue
    public String channel_id;
    @JsonValue
    public String field1;
//    @JsonValue
//    public String latitude;
//    @JsonValue
//    public String longitude;
//    @JsonValue
    public String name;
//    @JsonValue
//    public String metadata;
//    @JsonValue
//    public String created_at;
//    @JsonValue
//    public String updated_at;
//    @JsonValue
//    public String attached_at;
    @JsonValue
    public String last_values;
}
