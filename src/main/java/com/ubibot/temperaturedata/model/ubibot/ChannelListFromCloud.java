package com.ubibot.temperaturedata.model.ubibot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelListFromCloud {
    @JsonValue
    private String result;
    @JsonValue
    private String server_time;
    @JsonValue
    private List<ChannelDataFromCloud> channels;
}
