package com.ubibot.temperaturedata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubibot.temperaturedata.UbibotConfigProperties;
import com.ubibot.temperaturedata.model.ChannelDataFromCloud;
import com.ubibot.temperaturedata.model.ChannelListFromCloud;
import com.ubibot.temperaturedata.model.SensorDataToPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SensorDataService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    UbibotConfigProperties config;
    @Autowired
    ObjectMapper objectMapper;
    public void getChannelDataFromCloud() throws IOException {
        String requestUrl = config.WEB_API_URL() + "channels?account_key=" + config.ACCOUNT_KEY();
        ChannelListFromCloud response = restTemplate.getForObject(requestUrl, ChannelListFromCloud.class);
        assert response != null;

        // map the response data to a list of simplified objects
        List<SensorDataToPersist> preparedData = mapChannelDataToObject(response);

        // TODO: call a method to persist the prepared data to the database
        for (var chan : preparedData) {
            System.out.println(chan);
        }
    }

    private List<SensorDataToPersist> mapChannelDataToObject(ChannelListFromCloud response) throws JsonProcessingException {
        List<ChannelDataFromCloud> requestedChannels = response.getChannels();
        List<SensorDataToPersist> responseChannels = new ArrayList<>();

        // populate the responseChannels list
        for (int i = 0; i < requestedChannels.size(); i++) {
            ChannelDataFromCloud chan = requestedChannels.get(i);
            Map lastValues = objectMapper.readValue(chan.getLastValues(), Map.class);
            Object temperature = ((Map<String,Object>) lastValues.get("field1")).get("value");
            SensorDataToPersist channel = new SensorDataToPersist();
            channel.setChannelId(chan.getChannelId());
            channel.setName(chan.getName());
            channel.setFieldOneLabel(chan.getFieldOneLabel());
            channel.setTemperature(temperature.toString());
            channel.setServerTime(response.getServer_time());
            responseChannels.add(i, channel);
        }

        return responseChannels;
    }
}
