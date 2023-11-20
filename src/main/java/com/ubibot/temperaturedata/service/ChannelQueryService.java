package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.UbibotConfigProperties;
import com.ubibot.temperaturedata.model.ChannelsQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChannelQueryService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    UbibotConfigProperties config;
    @Value("channels?account_key=")
    private String channels;
    public void getChannelDataFromCloud() {
        String requestUrl = config.WEB_API_URL() + channels + config.ACCOUNT_KEY();
        System.out.println(requestUrl);
        ResponseEntity<ChannelsQueryResponse> response = restTemplate.getForEntity(requestUrl, ChannelsQueryResponse.class);
        System.out.println(response);
    }
}
