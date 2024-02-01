package com.ubibot.temperaturedata.model.client;


import com.ubibot.temperaturedata.model.database.BuildingData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientUnitRequest {

    public ClientUnitRequest(String unitId, String tenantName, String unitNumber, String fullUnit) {
        this.unitId = unitId;
        this.tenantName = tenantName;
        this.unitNumber = unitNumber;
        this.fullUnit = fullUnit;
    }

    private String unitId;

    private String tenantName;

    private String unitNumber;

    private BuildingData buildingId;

    private String fullUnit;
}
