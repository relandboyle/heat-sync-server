package com.ubibot.temperaturedata.model.ubibot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelListFromCloud {

    @JsonProperty("result")
    private String result;

    @JsonProperty("server_time")
    private String serverTime;

    @JsonProperty("channels")
    private List<ChannelDataFromCloud> channels;
}
