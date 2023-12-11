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
public class ClientUnitRequest {

    @JsonValue
    private String unitId;

    @JsonValue
    private String tenantName;

    @JsonValue
    private String unitNumber;

    @JsonValue
    private String buildingId;
}
