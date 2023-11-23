package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.aggregator.SensorDataAggregator;
import com.ubibot.temperaturedata.model.database.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/sensor")
public class SensorController {

    @Autowired
    SensorDataAggregator aggregator;

    @PostMapping
    String receiveChannelData(@RequestBody SensorData sensorData) {
        System.out.println("CONTROLLER INPUTS: ");
        System.out.println(sensorData);
        return aggregator.sensorDataManualEntry(sensorData);
    }
}
