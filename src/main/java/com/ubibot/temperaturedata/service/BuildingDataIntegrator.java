package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.model.client.ClientBuildingRequest;
import com.ubibot.temperaturedata.model.database.BuildingData;
import com.ubibot.temperaturedata.repository.BuildingRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class BuildingDataIntegrator {

    @Autowired
    BuildingRepository buildingRepository;

    public List<BuildingData> searchForBuilding(ClientBuildingRequest request) {
        String number = request.getStreetNumber();
        String name = request.getStreetName();
        String postal = request.getPostalCode();
        log.info("NUMBER, NAME, POSTAL: \n{}\n{}\n{}", number, name, postal);
        return buildingRepository.findByStreetNumberContainingOrStreetNameContainingOrPostalCodeContaining(number, name, postal);
    }

    public String createBuilding(BuildingData newBuilding) {
        log.info("newBuilding object received by BuildingDataIntegrator: {}", newBuilding);
        BuildingData confirmation = buildingRepository.save(newBuilding);
        return "New building created successfully";
    }
}
