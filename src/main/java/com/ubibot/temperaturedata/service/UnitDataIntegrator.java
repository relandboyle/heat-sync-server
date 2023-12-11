package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.model.client.ClientUnitRequest;
import com.ubibot.temperaturedata.model.database.BuildingData;
import com.ubibot.temperaturedata.model.database.UnitData;
import com.ubibot.temperaturedata.repository.BuildingRepository;
import com.ubibot.temperaturedata.repository.UnitRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class UnitDataIntegrator {

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    BuildingRepository buildingRepository;

    public List<UnitData> searchForUnit(ClientUnitRequest request) {
        return unitRepository.findByTenantNameContainingOrUnitNumberContaining(request.getTenantName(), request.getUnitNumber());
    }

    public String createOrUpdateUnit(ClientUnitRequest request, BuildingData existingBuilding) {
        log.info("AGGREGATOR - CREATE OR UPDATE UNIT: \n{}\n{}", request, existingBuilding);
        UnitData response = null;
        UnitData unitData = new UnitData();
        unitData.setUnitNumber(request.getUnitNumber());
        unitData.setTenantName(request.getTenantName());
        unitData.setBuilding(existingBuilding);
        try {
            response = unitRepository.save(unitData);
        } catch(Exception err) {
            log.error("An exception has occurred: {}", err.getMessage());
        }
        log.info("CREATED OR UPDATED UNIT: \nNAME: {}", response.getTenantName());
        return response.toString();
    }
}
