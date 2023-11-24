package com.ubibot.temperaturedata.schedule;

import com.ubibot.temperaturedata.aggregator.SensorDataAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SensorDataThirtyMinuteIntervals {
    @Autowired
    SensorDataAggregator service;

    @Scheduled(cron = "0/10 * * * * ?")
    public void getChannelData() throws IOException {
        service.getChannelDataFromCloud();
    }
}
