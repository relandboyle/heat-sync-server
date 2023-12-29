package com.ubibot.temperaturedata.domain;

import com.ubibot.temperaturedata.model.client.ClientUnitRequest;
import com.ubibot.temperaturedata.model.database.BuildingData;
import com.ubibot.temperaturedata.model.database.UnitData;
import com.ubibot.temperaturedata.repository.BuildingRepository;
import com.ubibot.temperaturedata.repository.UnitRepository;
import com.ubibot.temperaturedata.service.UnitDataIntegrator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class UnitDataAggregator {

    @Autowired
    UnitDataIntegrator integrator;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    BuildingRepository buildingRepository;

    public List<UnitData> searchForUnit(ClientUnitRequest request) {
        List<UnitData> searchResult = integrator.searchForUnit(request);
        // Update each result to remove the reference to a BuildingData object and replace with a buildingId string
        for (UnitData result : searchResult) {
            if (result.getBuilding() != null) {
                result.setBuildingId(result.getBuilding().getBuildingId());
                result.setBuilding(null);
            }
        }
        return searchResult;
    }

    public String createOrUpdateUnit(ClientUnitRequest request) {
        log.info("AGGREGATOR - CREATE OR UPDATE UNIT");
        Optional<BuildingData> existingBuilding = buildingRepository.findById(request.getBuildingId());
        String response = null;

        if (existingBuilding.isPresent()) {
            response = integrator.createOrUpdateUnit(request, existingBuilding.get());
        } else {
            response = "The selected Building does not exist. Please enter a valid Building ID.";
        }

        return response;
    }
}
