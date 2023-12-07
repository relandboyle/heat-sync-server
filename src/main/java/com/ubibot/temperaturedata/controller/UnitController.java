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

    @PostMapping(value = "newUnit")
    String createNewUnit(@RequestBody UnitData newUnit, @RequestParam String buildingId) {
        Optional<BuildingData> existingBuilding = buildingRepository.findById(buildingId);
        existingBuilding.ifPresent(newUnit::setBuilding);
        unitRepository.save(newUnit);
        return "A NEW UNIT HAS BEEN CREATED";
    }

    @PostMapping(value = "getUnit")
    String getUnitData() {
        String ID = "b3097e24-8542-4f55-a403-e5dabadfdefa";
        System.out.println(unitRepository.findById(ID).get());

        return "TEST";
    }

}
