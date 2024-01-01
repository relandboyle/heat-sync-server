package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.database.UnitData;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import com.ubibot.temperaturedata.repository.SensorDataRepository;
import com.ubibot.temperaturedata.repository.UnitRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class SensorIntegrator {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SensorDataRepository sensorDataRepository;

    @Autowired
    UnitRepository unitRepository;

    public void persistSensorData(List<SensorData> channelData) throws Exception {
        for (SensorData channel : channelData) {
            try {
                String sensorName = channel.getName();
                Optional<UnitData> unit = unitRepository.findById(sensorName);
                channel.setUnit(unit.get());
            } catch(Exception err) {
                log.error("An exception has occurred: {}", err.getMessage(), err);
            }
        }

        try {
            sensorDataRepository.saveAll(channelData);
        } catch(Exception err) {
            log.error("An exception has occurred: {}", err.getMessage(), err);
        }

        log.info("Sensor data persisted to database: {}", channelData);
    }

    public ChannelListFromCloud getCurrentChannelData(String url) {
        return restTemplate.getForObject(url, ChannelListFromCloud.class);
    }
}
