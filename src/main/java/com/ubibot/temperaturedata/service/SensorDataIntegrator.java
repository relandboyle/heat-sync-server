package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.database.UnitData;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import com.ubibot.temperaturedata.repository.SensorDataRepository;
import com.ubibot.temperaturedata.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class SensorDataIntegrator {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SensorDataRepository sensorDataRepository;

    @Autowired
    UnitRepository unitRepository;

    public String persistSensorData(List<SensorData> channelData) {
        for (SensorData channel : channelData) {
            String sensorName = channel.getName();
            Optional<UnitData> unit = unitRepository.findById(sensorName);
            channel.setUnit(unit.get());
        }
        sensorDataRepository.saveAll(channelData);
        return "ENTRY OR ENTRIES WERE ADDED TO THE SENSOR DATA TABLE";
    }

    public ChannelListFromCloud getCurrentChannelData(String url) {
        return restTemplate.getForObject(url, ChannelListFromCloud.class);
    }
}
