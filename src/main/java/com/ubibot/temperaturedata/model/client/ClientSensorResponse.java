package com.ubibot.temperaturedata.model.client;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javatuples.Pair;

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

    private Pair<Double, Double> flutterSpot;

    private String chartBottomTitle;

    public ClientSensorResponse(
            ZonedDateTime serverTime,
            ZonedDateTime createdAt,
            String entryId,
            String channelId,
            String fieldOneLabel,
            String name,
            String temperature,
            String latitude,
            String longitude,
            String outsideTemperature) {
        this.serverTime = serverTime;
        this.createdAt = createdAt;
        this.entryId = entryId;
        this.channelId = channelId;
        this.fieldOneLabel = fieldOneLabel;
        this.name = name;
        this.temperature = temperature;
        this.latitude = latitude;
        this.longitude = longitude;
        this.outsideTemperature = outsideTemperature;
    }
}
