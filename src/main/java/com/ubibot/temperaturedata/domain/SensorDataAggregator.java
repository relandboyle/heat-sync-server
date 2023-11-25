package com.ubibot.temperaturedata.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubibot.temperaturedata.UbibotConfigProperties;
import com.ubibot.temperaturedata.service.SensorDataIntegrator;
import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.database.UnitData;
import com.ubibot.temperaturedata.model.ubibot.ChannelDataFromCloud;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import com.ubibot.temperaturedata.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SensorDataAggregator {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    UbibotConfigProperties config;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SensorDataIntegrator integrator;

    @Autowired
    UnitRepository unitRepository;

    public String sensorDataManualEntry(SensorData sensorData) {
        System.out.println("HIT AGGREGATOR");
        String sensorName = sensorData.getName();
        Optional<UnitData> unit = unitRepository.findById(sensorName);
        System.out.println("UNIT: " + unit);
        sensorData.setUnit(unit.get());
        List<SensorData> sensorDataList = new ArrayList<>();
        sensorDataList.add(sensorData);
        System.out.println("LIST: " + sensorDataList);
        return integrator.persistSensorData(sensorDataList);
    }

    public String getChannelDataFromCloud() throws IOException {
        String requestUrl = config.WEB_API_URL() + "channels?account_key=" + config.ACCOUNT_KEY();
        ChannelListFromCloud channelList = restTemplate.getForObject(requestUrl, ChannelListFromCloud.class);
        assert channelList != null;

        // map the response data to a list of simplified objects
        List<SensorData> sensorDataList = mapChannelDataToSensorData(channelList);

        // give each sensor data entry a reference to a unit based on the sensor name
        for (SensorData sensor : sensorDataList) {
            String sensorName = sensor.getName();
            Optional<UnitData> unit = unitRepository.findById(sensorName);
            sensor.setUnit(unit.get());
        }

        // call a method to persist the prepared data to the database
        return integrator.persistSensorData(sensorDataList);
    }

    private List<SensorData> mapChannelDataToSensorData(ChannelListFromCloud response) throws JsonProcessingException {
        List<ChannelDataFromCloud> requestedChannels = response.getChannels();
        List<SensorData> responseChannels = new ArrayList<>();

        // populate the responseChannels list
        for (int i = 0; i < requestedChannels.size(); i++) {
            ChannelDataFromCloud chan = requestedChannels.get(i);
            Map lastValues = objectMapper.readValue(chan.getLastValues(), Map.class);
            Object temperature = ((Map<String,Object>) lastValues.get("field1")).get("value");
            SensorData channel = new SensorData();
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
