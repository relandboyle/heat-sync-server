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
import java.util.Optional;

@Log4j2
@Service
public class UnitIntegrator {

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    BuildingRepository buildingRepository;

    public List<UnitData> searchForUnit(ClientUnitRequest request) {
        log.info("\nFULL UNIT QUERY: {}\nBUILDING ID: {}", request.getFullUnit(), request.getBuildingId());
        // TODO: use an 'exists' query rather than get or find
        Optional<BuildingData> selectedBuilding = buildingRepository.findById(request.getBuildingId());

        List<UnitData> matchingUnitsInBuilding = unitRepository.findByBuildingIdContainingAndFullUnitIgnoreCaseContaining(request.getBuildingId(), request.getFullUnit());
        return matchingUnitsInBuilding;
    }

    public String createOrUpdateUnit(UnitData newUnit) throws Exception {
        log.info("UNIT INTEGRATOR - CREATE OR UPDATE UNIT");

        try {
            UnitData confirmation = unitRepository.save(newUnit);
            log.info("UNIT INTEGRATOR - SUCCESSFULLY CREATED OR UPDATED UNIT");
            return "New unit created successfully with ID: " + confirmation.getId();
        } catch(Exception err) {
            log.error("An exception has occurred: {}", err.getMessage());
            throw new Exception(err);
        }
    }

}
