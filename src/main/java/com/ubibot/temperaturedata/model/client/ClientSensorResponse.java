package com.ubibot.temperaturedata.model.client;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientSensorResponse {

    private ZonedDateTime serverTime;

    private ZonedDateTime createdAt;

    private String entryId;

    private String channelId;

    private String fieldOneLabel;

    private String name;

    private String temperature;

    private String latitude;

    private String longitude;

    private String outsideTemperature;
}
