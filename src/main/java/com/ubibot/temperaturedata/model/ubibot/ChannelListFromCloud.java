package com.ubibot.temperaturedata.model.ubibot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelListFromCloud {

    @JsonProperty("result")
    private String result;

    @JsonProperty("server_time")
    private ZonedDateTime serverTime;

    @JsonProperty("channels")
    private List<ChannelDataFromCloud> channels;
}
