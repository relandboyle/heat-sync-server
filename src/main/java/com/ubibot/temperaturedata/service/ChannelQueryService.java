package com.ubibot.temperaturedata.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubibot.temperaturedata.UbibotConfigProperties;
import com.ubibot.temperaturedata.model.ChannelDataFromCloud;
import com.ubibot.temperaturedata.model.ChannelListFromCloud;
import com.ubibot.temperaturedata.model.ChannelToClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChannelQueryService {
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

        List<ChannelDataFromCloud> requestedChannels = response.getChannels();
        List<ChannelToClient> responseChannels = new ArrayList<>();
        for (int i = 0; i < requestedChannels.size(); i++) {
            ChannelDataFromCloud chan = requestedChannels.get(i);
            Map lastValues = objectMapper.readValue(chan.getLast_values(), Map.class);
            Object temperature = ((Map<String,Object>) lastValues.get("field1")).get("value");
            ChannelToClient channel = new ChannelToClient();
            channel.setChannelId(chan.getChannel_id());
            channel.setName(chan.getName());
            channel.setFieldOneLabel(chan.getField1());
            channel.setTemperature(temperature.toString());
            channel.setServerTime(response.getServer_time());
            responseChannels.add(i, channel);
        }

        for (var chan : responseChannels) {
            System.out.println(chan);
        }
    }
}
