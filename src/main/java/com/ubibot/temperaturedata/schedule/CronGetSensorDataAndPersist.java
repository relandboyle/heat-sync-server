package com.ubibot.temperaturedata.schedule;

import com.ubibot.temperaturedata.domain.ScheduleAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CronGetSensorDataAndPersist {
    @Autowired
    ScheduleAggregator aggregator;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void getSensorDataAndPersist() throws Exception {
        aggregator.cronGetSensorDataAndPersist();
    }
}
