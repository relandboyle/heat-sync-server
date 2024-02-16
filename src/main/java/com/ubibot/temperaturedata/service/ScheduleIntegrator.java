package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import com.ubibot.temperaturedata.repository.SensorDataRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
@Service
public class ScheduleIntegrator {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SensorDataRepository sensorDataRepository;

    public ChannelListFromCloud getChannelDataFromCloud(String requestUrl) throws Exception {
        ChannelListFromCloud channelList;
        try {
            channelList = restTemplate.getForObject(requestUrl, ChannelListFromCloud.class);
        } catch(Exception err) {
            log.error("An exception has occurred: {}", err.getMessage());
            throw new Exception(err);
        }
        return channelList;
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
