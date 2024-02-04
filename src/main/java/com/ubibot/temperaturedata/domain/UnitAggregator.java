package com.ubibot.temperaturedata.domain;

import com.ubibot.temperaturedata.model.client.ClientUnitRequest;
import com.ubibot.temperaturedata.model.database.BuildingData;
import com.ubibot.temperaturedata.model.database.UnitData;
import com.ubibot.temperaturedata.repository.BuildingRepository;
import com.ubibot.temperaturedata.repository.UnitRepository;
import com.ubibot.temperaturedata.service.UnitIntegrator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class UnitAggregator {

    @Autowired
    UnitIntegrator integrator;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    BuildingRepository buildingRepository;

    public List<ClientUnitRequest> searchForUnit(ClientUnitRequest request) {
        // unit data returned from the database query
        List<UnitData> searchResult = integrator.searchForUnit(request);

        // map from UnitData to ClientUnitRequest - is this still valid?
        List<ClientUnitRequest> mappedResult = searchResult.stream()
                .map(unitData -> new ClientUnitRequest(
                        unitData.getId(),
                        unitData.getTenantName(),
                        unitData.getUnitNumber(),
                        unitData.getBuildingId(),
                        unitData.getFullUnit(),
                        unitData.getChannelId())).toList();
        log.info("STREAM CHECK: {}", mappedResult.get(0).getFullUnit());
        return mappedResult;
    }

    public String createOrUpdateUnit(ClientUnitRequest request) throws Exception {
        // should pass a UnitData object to Integrator which includes the buildingId and fullUnit
        log.info("AGGREGATOR - CREATE OR UPDATE UNIT");
        // get a reference to the existing building in the database
        Optional<BuildingData> existingBuilding = buildingRepository.findById(request.getBuildingId());

        UnitData newUnit = new UnitData();
        if (existingBuilding.isPresent()) {
            newUnit.setUnitNumber(request.getUnitNumber());
            newUnit.setTenantName(request.getTenantName());
            newUnit.setFullUnit(request.getUnitNumber() + ", " + request.getTenantName());
            newUnit.setBuildingData(existingBuilding.get());
            return integrator.createOrUpdateUnit(newUnit);
        } else {
            log.error("EXISTING BUILDING NOT FOUND: {}", request.getBuildingId());
            return "UNABLE TO CREATE OR UPDATE UNIT AS REQUESTED";
        }
    }
}
