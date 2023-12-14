package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.domain.UnitDataAggregator;
import com.ubibot.temperaturedata.model.client.ClientUnitRequest;
import com.ubibot.temperaturedata.model.database.UnitData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@CrossOrigin(origins = {"http://localhost:12345/", "https://heat-sync.net/"})
@RequestMapping("api/v1/unit")
public class UnitController {

    @Autowired
    UnitDataAggregator aggregator;

    @PostMapping("searchUnits")
    public List<UnitData> searchForUnit(@RequestBody ClientUnitRequest request) {
        log.info("SEARCH FOR UNITS: {}", request.toString());
        return aggregator.searchForUnit(request);
    }

    @PostMapping(value = "newUnit")
    public String createOrUpdateUnit(@RequestBody ClientUnitRequest request) {
        log.info("CREATE OR UPDATE UNIT: {}", request.toString());
        return aggregator.createOrUpdateUnit(request);
    }

    @PostMapping(value = "getUnit")
    public String getUnitData() {

        return "TEST";
    }

}
