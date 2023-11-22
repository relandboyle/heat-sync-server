package com.ubibot.temperaturedata.schedule;

import com.ubibot.temperaturedata.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SensorDataThirtyMinuteIntervals {
    @Autowired
    SensorDataService service;

    @Scheduled(cron = "6 0/30 * * * ?")
    public void getChannelData() throws IOException {
        service.getChannelDataFromCloud();
    }
}
