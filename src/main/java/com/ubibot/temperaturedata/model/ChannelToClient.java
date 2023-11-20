package com.ubibot.temperaturedata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelToClient {
    @JsonProperty("server_time")
    private String serverTime;
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty("field1")
    private String fieldOneLabel;
    @JsonProperty
    private String name;
    @JsonProperty
    private String temperature;
}
