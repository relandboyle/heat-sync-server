package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.model.database.BuildingData;
import com.ubibot.temperaturedata.model.database.UnitData;
import com.ubibot.temperaturedata.repository.BuildingRepository;
import com.ubibot.temperaturedata.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/unit")
public class UnitController {

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    BuildingRepository buildingRepository;

    @PostMapping
    String createNewUnit(@RequestBody UnitData newUnit, @RequestParam String buildingId) {
        Optional<BuildingData> existingBuilding = buildingRepository.findById(buildingId);
        existingBuilding.ifPresent(newUnit::setBuilding);
        unitRepository.save(newUnit);
        return "A NEW UNIT HAS BEEN CREATED";
    }
}
