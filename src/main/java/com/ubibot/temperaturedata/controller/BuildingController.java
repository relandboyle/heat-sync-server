package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.model.database.BuildingData;
import com.ubibot.temperaturedata.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("api/v1/building")
public class BuildingController {

    @Autowired
    BuildingRepository buildingRepository;

    @PostMapping
    public String createBuilding(@RequestBody BuildingData newBuilding) throws SQLException {
        buildingRepository.save(newBuilding);
        return "A NEW BUILDING WAS CREATED";
    }
}
