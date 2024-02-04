package com.ubibot.temperaturedata.model.client;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientSensorRequest {

    @JsonValue
    private ZonedDateTime dateRangeStart;

    @JsonValue
    private ZonedDateTime dateRangeEnd;

    @JsonValue
    private String buildingId;

    @JsonValue
    private String unitId;

    @JsonValue
    private String channelId;
}
