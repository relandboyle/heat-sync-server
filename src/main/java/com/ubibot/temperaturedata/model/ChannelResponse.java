package com.ubibot.temperaturedata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelResponse {
    public String channel_id;
    public String field1;
    public String latitude;
    public String longitude;
    public String name;
    public String metadata;
    public String created_at;
    public String updated_at;
    public String attached_at;
    public String last_values;
}
