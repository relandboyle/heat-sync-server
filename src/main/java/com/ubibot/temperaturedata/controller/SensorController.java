package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.domain.SensorAggregator;
import com.ubibot.temperaturedata.model.client.ClientSensorRequest;
import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@Log4j2
@CrossOrigin(origins = {"http://localhost:12345", "https://heat-sync.net"})
@RequestMapping(value = "api/v1/sensor", consumes = "application/json", produces = "application/json")
public class SensorController {

    @Autowired
    SensorAggregator aggregator;

    @GetMapping("/currentChannelData")
    ChannelListFromCloud getCurrentChannelData(@RequestParam String accountKey) throws URISyntaxException {
        System.out.println(accountKey);
        log.info("TESTING LOG MESSAGE");
        return aggregator.getCurrentChannelData(accountKey);
    }

    @PostMapping("/filteredSensorData")
    List<ClientSensorRequest> getFilteredChannelData(@RequestBody ClientSensorRequest request) throws Exception {
        log.info("CONTROLLER: request: {}", request.getDateRangeStart());
        List<ClientSensorRequest> response = aggregator.getFilteredChannelData(request);
        log.info("RESPONSE SIZE: {}", response.size());
        return response;
    }

    @PostMapping("/newSensor")
    String receiveChannelData(@RequestBody SensorData sensorData) throws Exception {
        System.out.println("CONTROLLER INPUTS: ");
        System.out.println(sensorData);
        return aggregator.manualGetSensorDataAndPersist(sensorData);
    }
}
