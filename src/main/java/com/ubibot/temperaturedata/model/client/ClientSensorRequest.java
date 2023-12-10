package com.ubibot.temperaturedata.model.client;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientSensorRequest {

    @JsonValue
    private String dateRangeStart;

    @JsonValue
    private String dateRangeEnd;

    @JsonValue
    private String buildingId;

    @JsonValue
    private String unitId;
}
