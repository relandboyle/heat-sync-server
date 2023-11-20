package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.UbibotConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QueryDataThirtyMinutes {
    @Autowired
    UbibotConfigProperties ubibot;
    @Autowired
    ChannelQueryService service;

    @Scheduled(cron = "2 0/10 * * * ?")
    public void getChannelData() throws IOException {
        service.getChannelDataFromCloud();
    }
}
