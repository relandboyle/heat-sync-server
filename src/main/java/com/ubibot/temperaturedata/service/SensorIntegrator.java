package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import com.ubibot.temperaturedata.repository.SensorDataRepository;
import com.ubibot.temperaturedata.repository.UnitRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.List;

@Log4j2
@Service
public class SensorIntegrator {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SensorDataRepository sensorDataRepository;

    @Autowired
    UnitRepository unitRepository;

    public List<SensorData> getFilteredChannelDataByIdAndDateRange(String channelId, ZonedDateTime dateStart, ZonedDateTime dateEnd) {
        return sensorDataRepository.findByChannelIdAndServerTimeIsBetweenOrderByServerTimeAsc(channelId, dateStart, dateEnd);
    }

    public List<SensorData> getFilteredChannelDataById(String channelId) {
        return sensorDataRepository.findByChannelIdOrderByServerTimeDesc(channelId);
    }

    public List<SensorData> getFilteredChannelDataByDateRange(ZonedDateTime dateStart, ZonedDateTime dateEnd) {
        return sensorDataRepository.findByServerTimeIsBetweenOrderByServerTimeDesc(dateStart, dateEnd);
    }

    public void persistSensorData(List<SensorData> channelData) throws Exception {
        try {
            sensorDataRepository.saveAll(channelData);
        } catch(Exception err) {
            log.error("An exception has occurred: '{}' when persisting sensor data", err.getMessage());
            throw new Exception(err);
        }

        log.info("Sensor data persisted to database: {}", channelData);
    }

    public ChannelListFromCloud getCurrentChannelData(String url) {
        return restTemplate.getForObject(url, ChannelListFromCloud.class);
    }
}
