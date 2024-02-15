package com.ubibot.temperaturedata.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubibot.temperaturedata.UbibotConfigProperties;
import com.ubibot.temperaturedata.model.client.ClientSensorData;
import com.ubibot.temperaturedata.model.client.ClientSensorRequest;
import com.ubibot.temperaturedata.model.client.ClientSensorResponse;
import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.ubibot.ChannelDataFromCloud;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import com.ubibot.temperaturedata.service.SensorIntegrator;
import lombok.extern.log4j.Log4j2;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Log4j2
@Service
public class SensorAggregator {

    @Autowired
    private Properties properties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UbibotConfigProperties config;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SensorIntegrator integrator;

    @Autowired
    private WeatherServiceAggregator nwsAggregator;

    // return a list of sensor data entries based on user inputs
    public ClientSensorResponse getFilteredChannelData(ClientSensorRequest request) throws Exception {
        if (request.getChannelId().isEmpty()) {
            request.setChannelId(null);
        }
        log.info(
                "GET FILTERED CHANNEL DATA: {}, {}, {}",
                request.getChannelId(),
                request.getDateRangeStart(),
                request.getDateRangeEnd());

        // initialize a list of type SensorData to return
        List<SensorData> response = new ArrayList<>();

        Double myNumber = 12.5;
        log.info(myNumber.getClass());

        // if a channelId AND a date range are provided
        // return data for only that unit, only within that date range
        if (request.getChannelId() != null && request.getDateRangeStart() != null && request.getDateRangeEnd() != null) {
            try {
                ZonedDateTime dateStart = request.getDateRangeStart();
                ZonedDateTime dateEnd =request.getDateRangeEnd();
                String channelId = request.getChannelId();
                log.info("\n NAME: {} \n DATES: {}, {}", channelId, dateStart, dateEnd);
                response = integrator.getFilteredChannelDataByIdAndDateRange(channelId, dateStart, dateEnd);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

         // if only a date range is provided
         // return all data from within the date range
        else if (request.getChannelId() == null && request.getDateRangeStart() != null && request.getDateRangeEnd() != null) {
            try {
                ZonedDateTime dateStart = request.getDateRangeStart();
                ZonedDateTime dateEnd =request.getDateRangeEnd();
                log.info("DATES: {}, {}", dateStart, dateEnd);
                response = integrator.getFilteredChannelDataByDateRange(dateStart, dateEnd);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

//         if only a channelId is provided
//         return all data for the selected unit
        else if (request.getChannelId() != null && request.getDateRangeStart() == null || request.getDateRangeEnd() == null) {
            try {
                String channelId = request.getChannelId();
                log.info("CHANNEL ID: {}", channelId);
                response = integrator.getFilteredChannelDataById(channelId);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

        log.info("RESPONSE SIZE: {}", response.size());


        List<ClientSensorData> mappedResponse = mapSensorDataToClientSensorResponse(response);
        List<ClientSensorData> responseWithSpots = flutterSpotGenerator(mappedResponse);
        ClientSensorResponse responseWithSpacer = new ClientSensorResponse();
        responseWithSpacer.setSensorData(responseWithSpots);
        responseWithSpacer.setBottomTileSpacer(bottomTileSpacerGenerator(responseWithSpots));

        return responseWithSpacer;
    }
    // end getFilteredChannelData

    public String manualGetSensorDataAndPersist(SensorData sensorData) throws Exception {
        System.out.println("HIT AGGREGATOR");
        List<SensorData> sensorDataList = new ArrayList<>();
        sensorDataList.add(sensorData);
        System.out.println("LIST: " + sensorDataList);
        try {
            integrator.persistSensorData(sensorDataList);
        } catch(Exception err) {
            log.error("An exception has occurred: {}", err.getMessage(), err);
            throw new Exception(err);
        }
        return "Sensor data has been persisted to the database.";
    }

    public void cronGetSensorDataAndPersist() throws Exception {
        String requestUrl = "";
        String webApiUrl = config.WEB_API_URL();
        String accountKey = config.ACCOUNT_KEY();
        try {
            requestUrl = String.valueOf(
                    new URI("https", webApiUrl, "/channels", "account_key=" + accountKey, null));
        } catch(URISyntaxException err) {
            log.error("An exception has occurred: {}", err.getMessage());
            throw new Exception(err);
        }

        // get the current data from all sensors on the account
        ChannelListFromCloud channelList;
        try {
            channelList = restTemplate.getForObject(requestUrl, ChannelListFromCloud.class);
        } catch(Exception err) {
            log.error("An exception has occurred: {}", err.getMessage());
            throw new Exception(err);
        }

        // map the response data to a list of simplified objects
        List<SensorData> sensorDataList = new ArrayList<>();
        try {
            sensorDataList = channelList != null ? mapChannelDataToSensorData(channelList) : null;
            assert sensorDataList != null;
        } catch(JsonProcessingException err) {
            log.error("An exception has occurred: {}", err.getMessage(), err);
        }

        // get the outside air temperature for each entry
        List<SensorData> sensorDataListWithOutsideAirTemps = nwsAggregator.setOutsideAirTemperature(sensorDataList);

        // call a method to persist the prepared data to the database
        integrator.persistSensorData(sensorDataListWithOutsideAirTemps);
    }

    // takes sensor 'latest values' from the cloud and formats as SensorData objects
    private List<SensorData> mapChannelDataToSensorData(ChannelListFromCloud response) throws JsonProcessingException {
        // create a new list of SensorData to return
        List<SensorData> responseChannels = new ArrayList<>();

        // populate the responseChannels list
        // iterate over the list of sensor last values
        for (ChannelDataFromCloud chan : response.getChannels()) {
            System.out.println(System.currentTimeMillis());

            HashMap<?, ?> lastValues = objectMapper.readValue(chan.getLastValues(), HashMap.class);
            Object temperature = ((HashMap<?, ?>) lastValues.get("field1")).get("value");
            Object createdAt = ((HashMap<?, ?>) lastValues.get("field1")).get("created_at");
            log.info("CREATED AT: {} \n {}", createdAt, ZonedDateTime.parse(createdAt.toString()));

            SensorData channel = new SensorData();
            channel.setChannelId(chan.getChannelId());
            channel.setName(chan.getName());
            channel.setFieldOneLabel(chan.getFieldOneLabel());
            channel.setTemperature(temperature.toString());
            channel.setCreatedAt(ZonedDateTime.parse(createdAt.toString()));
            channel.setLatitude(chan.getLatitude());
            channel.setLongitude(chan.getLongitude());
            channel.setServerTime(response.getServerTime());
            responseChannels.add(0, channel);
        }
        return responseChannels;
    }

    // reformats SensorData object as ClientSensorRequest object
    private List<ClientSensorData> mapSensorDataToClientSensorResponse(List<SensorData> entries) {
        // map from SensorData to ClientSensorRequest
        List<ClientSensorData> mappedResult = entries.stream()
                .map(entry -> new ClientSensorData(
                        entry.getServerTime().toInstant().toEpochMilli(),
                        entry.getCreatedAt().toInstant().toEpochMilli(),
                        entry.getEntryId(),
                        entry.getChannelId(),
                        entry.getFieldOneLabel(),
                        entry.getName(),
                        entry.getTemperature(),
                        entry.getLatitude(),
                        entry.getLongitude(),
                        entry.getOutsideTemperature()
                ))
                .toList();
        log.info("SENSOR AGGREGATOR: STREAM CHECK: {}", mappedResult.get(0).getChannelId());
        return mappedResult;
    }

    // add a FlutterSpot to each entry in the response data
    private List<ClientSensorData> flutterSpotGenerator(List<ClientSensorData> response) {
        int bottomTileSpacer = response.size() / 10;
        log.info("SIZE: {}", bottomTileSpacer);
        for (int i = 0; i < response.size(); i++) {
            ClientSensorData entry = response.get(i);
            Long spotServerTime = entry.getServerTime();
            Double spotTemperature = Double.parseDouble(entry.getTemperature());
            entry.setFlutterSpot(new Pair<>(spotServerTime, spotTemperature));

        }
        return response;
    }

    // generate a list of doubles from serverTime entries
    private List<Long> bottomTileSpacerGenerator(List<ClientSensorData> response) {
        int responseSize = response.size();
        int bottomTileSpacer = responseSize / 10;
        List<Long> spacer = new ArrayList<>();

        for (int i = 0; i < responseSize; i++) {
            ClientSensorData entry = response.get(i);
            if (i % bottomTileSpacer == 0) {
                spacer.add(entry.getServerTime());
            }
        }
        log.info("SPACER: {}, {}, {}", bottomTileSpacer, spacer.size(), spacer.toString());
        return spacer;
    }
}
