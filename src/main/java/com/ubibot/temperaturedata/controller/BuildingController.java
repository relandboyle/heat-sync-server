package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.domain.BuildingDataAggregator;
import com.ubibot.temperaturedata.model.client.ClientBuildingRequest;
import com.ubibot.temperaturedata.model.database.BuildingData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("api/v1/building")
public class BuildingController {

    @Autowired
    BuildingDataAggregator aggregator;

    @PostMapping("searchBuildings")
    public List<BuildingData> searchForBuilding(@RequestBody ClientBuildingRequest request) {
        System.out.println("SEARCH BUILDING REQUEST: " + request.toString());
        return aggregator.searchForBuilding(request);
    }

    @PostMapping("newBuilding")
    public String createBuilding(@RequestBody BuildingData newBuilding) {
        log.info("CREATE BUILDING: {}", newBuilding);
        return aggregator.createBuilding(newBuilding);
    }
}
