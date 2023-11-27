package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.domain.SensorDataAggregator;
import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/sensor")
public class SensorController {

    @Autowired
    SensorDataAggregator aggregator;

    @GetMapping
    ChannelListFromCloud getCurrentChannelData(@RequestParam String accountKey) {
        return aggregator.getCurrentChannelData(accountKey);
    }

    @PostMapping
    String receiveChannelData(@RequestBody SensorData sensorData) {
        System.out.println("CONTROLLER INPUTS: ");
        System.out.println(sensorData);
        return aggregator.sensorDataManualEntry(sensorData);
    }
}
