package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.domain.SensorDataAggregator;
import com.ubibot.temperaturedata.model.client.ClientSensorRequest;
import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@Log4j2
@CrossOrigin(origins = {"http://localhost:51253", "https://heat-sync.net"})
@RequestMapping("api/v1/sensor")
public class SensorController {

    @Autowired
    SensorDataAggregator aggregator;

    @GetMapping(value = "currentChannelData")
    ChannelListFromCloud getCurrentChannelData(@RequestParam String accountKey) throws URISyntaxException {
        System.out.println(accountKey);
        log.info("TESTING LOG MESSAGE");
        return aggregator.getCurrentChannelData(accountKey);
    }

    @PostMapping(value = "filteredSensorData")
    SensorData getFilteredChannelData(@RequestBody ClientSensorRequest request) {
        System.out.println("CONTROLLER");
        return aggregator.getFilteredChannelData(request);
    }

    @PostMapping(value = "newSensor")
    String receiveChannelData(@RequestBody SensorData sensorData) {
        System.out.println("CONTROLLER INPUTS: ");
        System.out.println(sensorData);
        return aggregator.sensorDataManualEntry(sensorData);
    }
}
