package com.ubibot.temperaturedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelListFromCloud {
    @JsonValue
    public String result;
    @JsonValue
    public String server_time;
    @JsonValue
    public List<ChannelDataFromCloud> channels;
}
