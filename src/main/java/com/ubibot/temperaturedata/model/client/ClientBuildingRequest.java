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

    private String id;

    private String streetNumber;

    private String streetName;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    private String fullAddress;
}
