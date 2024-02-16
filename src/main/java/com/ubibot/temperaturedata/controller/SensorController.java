package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.domain.SensorAggregator;
import com.ubibot.temperaturedata.model.client.ClientSensorRequest;
import com.ubibot.temperaturedata.model.client.ClientSensorResponse;
import com.ubibot.temperaturedata.model.database.SensorData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@CrossOrigin(origins = {"http://localhost:12345", "https://heat-sync.net"})
@RequestMapping(value = "api/v1/sensor", consumes = "application/json", produces = "application/json")
public class SensorController {

    @Autowired
    SensorAggregator aggregator;

    @PostMapping("/filteredSensorData")
    ClientSensorResponse getFilteredChannelData(@RequestBody ClientSensorRequest request) throws Exception {
        log.info("CONTROLLER: request: {}", request.getDateRangeStart());
        return aggregator.getFilteredChannelData(request);
    }

    @PostMapping("/newSensor")
    String receiveChannelData(@RequestBody SensorData sensorData) throws Exception {
        log.info("CONTROLLER INPUTS: {}", sensorData.getChannelId());
        return aggregator.manualGetSensorDataAndPersist(sensorData);
    }
}
