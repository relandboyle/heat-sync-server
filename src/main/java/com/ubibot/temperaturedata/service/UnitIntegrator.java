package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.model.client.ClientUnitRequest;
import com.ubibot.temperaturedata.model.database.UnitData;
import com.ubibot.temperaturedata.repository.BuildingRepository;
import com.ubibot.temperaturedata.repository.UnitRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class UnitIntegrator {

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    BuildingRepository buildingRepository;

    public List<UnitData> searchForUnit(ClientUnitRequest request) {
        String buildingId = request.getBuildingId();
        String fullUnit = request.getFullUnit();
        List<UnitData> unitSearchResults = unitRepository.findByBuildingIdAndFullUnitIgnoreCaseContaining(buildingId, fullUnit);
        log.info("unitSearchResults: FULL UNIT: {}", unitSearchResults.get(0).getFullUnit());
        return unitSearchResults;
    }

    public String createOrUpdateUnit(UnitData newUnit) throws Exception {
        log.info("UNIT INTEGRATOR - CREATE OR UPDATE UNIT");

        try {
            UnitData persistedUnit = unitRepository.save(newUnit);
            log.info("UNIT INTEGRATOR - SUCCESSFULLY CREATED OR UPDATED UNIT");
            return "New unit created successfully with ID: " + persistedUnit.getId();
        } catch (Exception err) {
            log.error("An exception has occurred: {}", err.getMessage());
            throw new Exception(err);
        }
    }

}
