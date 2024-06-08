package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.repository.SensorDataRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class SensorIntegrator {

    @Autowired
    SensorDataRepository sensorDataRepository;

    public ArrayList<SensorData> getFilteredChannelDataByIdAndDateRange(String channelId, ZonedDateTime dateStart, ZonedDateTime dateEnd) {
        return sensorDataRepository.findByChannelIdAndServerTimeIsBetweenOrderByServerTimeAsc(channelId, dateStart, dateEnd);
    }

    public ArrayList<SensorData> getFilteredChannelDataById(String channelId) {
        return sensorDataRepository.findByChannelIdOrderByServerTimeAsc(channelId);
    }

    public ArrayList<SensorData> getFilteredChannelDataByDateRange(ZonedDateTime dateStart, ZonedDateTime dateEnd) {
        return sensorDataRepository.findByServerTimeIsBetweenOrderByServerTimeAsc(dateStart, dateEnd);
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
}
