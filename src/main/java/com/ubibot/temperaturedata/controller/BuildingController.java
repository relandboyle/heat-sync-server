package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.domain.BuildingDataAggregator;
import com.ubibot.temperaturedata.model.database.BuildingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/building")
public class BuildingController {

    @Autowired
    BuildingDataAggregator aggregator;

    @PostMapping
    public String createBuilding(@RequestBody BuildingData newBuilding) {
        return aggregator.createBuilding(newBuilding);
    }
}
