package com.ubibot.temperaturedata.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubibot.temperaturedata.UbibotConfigProperties;
import com.ubibot.temperaturedata.model.client.ClientSensorRequest;
import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.database.UnitData;
import com.ubibot.temperaturedata.model.ubibot.ChannelDataFromCloud;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import com.ubibot.temperaturedata.repository.BuildingRepository;
import com.ubibot.temperaturedata.repository.SensorDataRepository;
import com.ubibot.temperaturedata.repository.UnitRepository;
import com.ubibot.temperaturedata.service.SensorDataIntegrator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Log4j2
@Service
public class SensorDataAggregator {

    @Autowired
    Properties properties;

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

    @Autowired
    SensorDataRepository sensorDataRepository;

    @Autowired
    BuildingRepository buildingRepository;

    public SensorData getFilteredChannelData(ClientSensorRequest request) {
        System.out.println("REQUEST: " + request);
//        List<SensorData> filteredChannelData = new ArrayList<>();
        // if a date range is provided
        // require a building_id OR a unit_id
        // return all data from selected date range for selected building or unit

        // if a building_id is provided
        // return all data for all units associated with the selected building
//        BuildingData selectedBuilding = buildingRepository.findById(request.getBuildingId()).get();


        // if a unit_id is provided
        // return all data for the selected unit
//        System.out.println(request.getUnitId() != null);
//        if (request.getUnitId() != null) {
            return sensorDataRepository.findById("a703e0bc-7d79-4f96-8d02-4beeec525446").get();

//        }

//        return filteredChannelData;
    }

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
            channel.setServerTime(response.getServerTime());
            responseChannels.add(i, channel);
        }

        return responseChannels;
    }

    public ChannelListFromCloud getCurrentChannelData(String accountKey) throws URISyntaxException {
        String webApiUrl = config.WEB_API_URL();
        String requestUrl = String.valueOf(new URI( "https", webApiUrl, "/channels", "account_key=" + accountKey, null));
        log.info("TESTING URI CONSTRUCTION: {}", requestUrl);

        ChannelListFromCloud currentData = integrator.getCurrentChannelData(requestUrl);
        List<ChannelDataFromCloud> channelList = currentData.getChannels();
        for (var channel : channelList) {
            channel.setTemperatureValue(extractTemperatureFromLastValues(channel.getLastValues()));
            channel.setLastValues(null);
        }
        return currentData;
    }

    private String extractTemperatureFromLastValues(String lastValues) {
        System.out.println(lastValues);
        return "25";
    }
}
