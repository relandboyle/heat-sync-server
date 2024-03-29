package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.domain.BuildingAggregator;
import com.ubibot.temperaturedata.model.client.ClientBuildingRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@CrossOrigin(origins = {"http://localhost:12345/", "https://heat-sync.net/"})
@RequestMapping("api/v1/building")
public class BuildingController {

    @Autowired
    BuildingAggregator aggregator;

    @PostMapping("searchBuildings")
    @Cacheable(cacheNames = "BuildingCache", unless = "#result == null")
    public List<ClientBuildingRequest> searchForBuilding(@RequestBody ClientBuildingRequest buildingQuery) {
        log.info("BUILDING CONTROLLER - SEARCH FOR BUILDING - Query: {}",
                buildingQuery.getFullAddress());
        List<ClientBuildingRequest> response = aggregator.searchForBuilding(buildingQuery);
        log.info("RESPONSE TO BUILDING REQUEST: {}", response.get(0).getFullAddress());
        return response;
    }

    @PostMapping("newBuilding")
    public String createBuilding(@RequestBody ClientBuildingRequest newBuilding) {
        log.info("CREATE BUILDING: {}", newBuilding);
        return aggregator.createBuilding(newBuilding);
    }
}
