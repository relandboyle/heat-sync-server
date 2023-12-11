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

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
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

    // return a list of sensor data entries based on user inputs
    public List<SensorData> getFilteredChannelData(ClientSensorRequest request) throws Exception {
        log.info("GETFILTEREDCHANNELDATA: {}", request);
        List<SensorData> response = null;

        // if a unit_id AND a date range are provided
        // return data for only that unit, only within that date range
        if (request.getUnitId() != null && request.getDateRangeStart() != null && request.getDateRangeEnd() != null) {
            try {
                ZonedDateTime dateStart = request.getDateRangeStart();
                ZonedDateTime dateEnd =request.getDateRangeEnd();
                String name = request.getUnitId();
                log.info("NAME: {}\nDATES: {}, {}", name, dateStart, dateEnd);
                response = sensorDataRepository.findByNameAndServerTimeIsBetween(name, dateStart, dateEnd);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

         // if only a date range is provided
         // return all data from within the date range
        if (request.getDateRangeStart() != null && request.getDateRangeEnd() != null && request.getUnitId() == null) {
            try {
                ZonedDateTime dateStart = request.getDateRangeStart();
                ZonedDateTime dateEnd =request.getDateRangeEnd();
                log.info("DATES: {}, {}", dateStart, dateEnd);
                response = sensorDataRepository.findByServerTimeBetween(dateStart, dateEnd);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

//         if only a unit_id is provided
//         return all data for the selected unit
        if (request.getUnitId() != null && request.getDateRangeStart() == null || request.getDateRangeEnd() == null) {
            try {
                String name = request.getUnitId();
                log.info("NAME: {}", name);
                response = sensorDataRepository.findByName(name);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

        return response;
    }

    public String manualGetSensorDataAndPersist(SensorData sensorData) throws Exception {
        System.out.println("HIT AGGREGATOR");
        String sensorName = sensorData.getName();
        Optional<UnitData> unit = unitRepository.findById(sensorName);
        System.out.println("UNIT: " + unit);
        sensorData.setUnit(unit.get());
        List<SensorData> sensorDataList = new ArrayList<>();
        sensorDataList.add(sensorData);
        System.out.println("LIST: " + sensorDataList);
        try {
            integrator.persistSensorData(sensorDataList);
        } catch(Exception err) {
            log.error("An exception has occurred: {}", err.getMessage(), err);
        }
        return "Sensor data has been persisted to the database.";
    }

    public void cronGetSensorDataAndPersist() throws Exception {
        String requestUrl = "";
        String webApiUrl = config.WEB_API_URL();
        String accountKey = config.ACCOUNT_KEY();
        try {
            requestUrl = String.valueOf(new URI( "https", webApiUrl, "/channels", "account_key=" + accountKey, null));
        } catch(URISyntaxException err) {
            log.error("An exception has occurred: {}", err.getMessage(), err);
        }

        // get the current data from all sensors on the account
        ChannelListFromCloud channelList = restTemplate.getForObject(requestUrl, ChannelListFromCloud.class);
        assert channelList != null;

        // map the response data to a list of simplified objects
        List<SensorData> sensorDataList = new ArrayList<>();
        try {
            sensorDataList = mapChannelDataToSensorData(channelList);
            // give each sensor data entry a reference to an existing unit based on the sensor name
            for (SensorData sensor : sensorDataList) {
                String sensorName = sensor.getName();
                Optional<UnitData> unit = unitRepository.findById(sensorName);
                sensor.setUnit(unit.get());
            }
        } catch(JsonProcessingException err) {
            log.error("An exception has occurred: {}", err.getMessage(), err);
        }

        // call a method to persist the prepared data to the database
        integrator.persistSensorData(sensorDataList);
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

    // calls the UbiBot web API to get last values from all sensors on the designated account
    public ChannelListFromCloud getCurrentChannelData(String accountKey) throws URISyntaxException {
        String webApiUrl = config.WEB_API_URL();
        String requestUrl = String.valueOf(new URI( "https", webApiUrl, "/channels", "account_key=" + accountKey, null));
        log.info("TESTING URI CONSTRUCTION: {}", requestUrl);

        ChannelListFromCloud currentData = integrator.getCurrentChannelData(requestUrl);
        List<ChannelDataFromCloud> channelList = currentData.getChannels();
        for (var channel : channelList) {
            channel.setTemperatureValue(extractTemperatureFromLastValues(channel.getLastValues()));
//            channel.setLastValues(null);
        }
        return currentData;
    }

    private String extractTemperatureFromLastValues(String lastValues) {
        log.info("LAST VALUES: {}", lastValues);
        ObjectMapper jsonMapper = new ObjectMapper();

        return "25";
    }
}
