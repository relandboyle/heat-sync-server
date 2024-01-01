package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.model.client.ClientBuildingRequest;
import com.ubibot.temperaturedata.model.database.BuildingData;
import com.ubibot.temperaturedata.repository.BuildingRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class BuildingIntegrator {

    @Autowired
    BuildingRepository buildingRepository;

    @Cacheable(cacheNames = "BuildingCache", unless = "#result == null")
    public List<BuildingData> searchForBuilding(ClientBuildingRequest request) {
        log.info("Method: searchForBuilding, fullAddress: {}",
                request.getFullAddress());
        return buildingRepository.findByFullAddressIgnoreCaseContaining(request.getFullAddress());
    }

    public String createBuilding(BuildingData newBuilding) {
        log.info("newBuilding object received by BuildingDataIntegrator: {}", newBuilding);
        BuildingData confirmation = buildingRepository.save(newBuilding);
        log.info("CONFIRMATION: {}", confirmation);
        return "New building created successfully with ID: " +  confirmation.getBuildingId();
    }
}
