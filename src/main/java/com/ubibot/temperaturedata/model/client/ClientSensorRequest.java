package com.ubibot.temperaturedata.model.client;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ClientSensorRequest {

    private ZonedDateTime dateRangeStart;

    private ZonedDateTime dateRangeEnd;

    private String channelId;
}
