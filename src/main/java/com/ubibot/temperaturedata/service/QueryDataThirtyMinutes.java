package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.UbibotConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class QueryDataThirtyMinutes {
    @Autowired
    UbibotConfigProperties ubibot;
    @Autowired
    ChannelQueryService service;

    @Scheduled(cron = "0/10 * * * * ?")
    public void getChannelData() {
        Date date = new Date();
        System.out.println("Testing Cron Job " + date.toString());
        Map<String, String> varMap = Map.of(
                "Account Key", ubibot.ACCOUNT_KEY(),
                "Web API URL", ubibot.WEB_API_URL());
        System.out.println(varMap);
        service.getChannelDataFromCloud();
    }
}
