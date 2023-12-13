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
public class ClientBuildingRequest {

    @JsonValue
    private String buildingId;

    @JsonValue
    private String streetNumber;

    @JsonValue
    private String streetName;

    @JsonValue
    private String city;

    @JsonValue
    private String state;

    @JsonValue
    private String postalCode;

    @JsonValue
    private String fullAddress;
}
