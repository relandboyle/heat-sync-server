package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.model.database.BuildingData;
import com.ubibot.temperaturedata.repository.BuildingRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BuildingDataIntegrator {

    @Autowired
    BuildingRepository repository;

    public String createBuilding(BuildingData newBuilding) {
        log.info("newBuilding object received by BuildingDataIntegrator: {}", newBuilding);
        BuildingData confirmation = repository.save(newBuilding);
        return "New building created successfully";
    }
}
