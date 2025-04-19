package com.ubibot.temperaturedata.domain;

import com.ubibot.temperaturedata.UbibotConfigProperties;
import com.ubibot.temperaturedata.model.client.ClientSensorData;
import com.ubibot.temperaturedata.model.client.ClientSensorRequest;
import com.ubibot.temperaturedata.model.client.ClientSensorResponse;
import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.service.SensorIntegrator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class SensorAggregator {

    @Autowired
    private UbibotConfigProperties ubibotConfig;

    @Autowired
    private SensorIntegrator sensorIntegrator;

    @Autowired
    private WeatherServiceAggregator weatherServiceAggregator;

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
        var response = new ArrayList<SensorData>();

        // if a channelId AND a date range are provided
        // return data for only that unit, only within that date range
        if (request.getChannelId() != null && request.getDateRangeStart() != null && request.getDateRangeEnd() != null) {
            try {
                ZonedDateTime dateStart = request.getDateRangeStart();
                ZonedDateTime dateEnd =request.getDateRangeEnd();
                String channelId = request.getChannelId();
                log.info("\n NAME: {} \n DATES: {}, {}", channelId, dateStart, dateEnd);
                response = sensorIntegrator.getFilteredChannelDataByIdAndDateRange(channelId, dateStart, dateEnd);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

         // if ONLY a date range is provided
         // return all data from within the date range
        else if (request.getChannelId() == null && request.getDateRangeStart() != null && request.getDateRangeEnd() != null) {
            try {
                ZonedDateTime dateStart = request.getDateRangeStart();
                ZonedDateTime dateEnd =request.getDateRangeEnd();
                log.info("DATES: {}, {}", dateStart, dateEnd);
                response = sensorIntegrator.getFilteredChannelDataByDateRange(dateStart, dateEnd);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

//         if ONLY a channelId is provided
//         return all data for the selected unit
        else if (request.getChannelId() != null && request.getDateRangeStart() == null || request.getDateRangeEnd() == null) {
            try {
                String channelId = request.getChannelId();
                log.info("CHANNEL ID: {}", channelId);
                response = sensorIntegrator.getFilteredChannelDataById(channelId);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

        log.info("RESPONSE SIZE: {}", response.size());


        var mappedResponse = mapSensorDataToClientSensorResponse(response);
        var responseWithSpacer = new ClientSensorResponse();
        responseWithSpacer.setSensorData(mappedResponse);
        responseWithSpacer.setBottomTitleSpacer(bottomTitleSpacerGenerator(mappedResponse));
        return responseWithSpacer;
    }
    // end getFilteredChannelData

    public String manualGetSensorDataAndPersist(SensorData sensorData) throws Exception {
        var sensorDataList = new ArrayList<SensorData>();
        sensorDataList.add(sensorData);
        try {
            sensorIntegrator.persistSensorData(sensorDataList);
        } catch(Exception err) {
            log.error("An exception has occurred: {}", err.getMessage(), err);
            throw new Exception(err);
        }
        return "Sensor data has been persisted to the database.";
    }

    // reformats SensorData object as ClientSensorRequest object
    private List<ClientSensorData> mapSensorDataToClientSensorResponse(List<SensorData> entries) {
        // map from SensorData to ClientSensorRequest
        return entries.stream()
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
                )).toList();
    }

    // generate a list of doubles from serverTime entries
    private List<Long> bottomTitleSpacerGenerator(List<ClientSensorData> response) {
        var spacer = new ArrayList<Long>();
        int responseSize = response.size();
        int bottomTitleSpacer = Math.max(responseSize / 10, 1);

        for (int i = 0; i < responseSize; i++) {
            var entry = response.get(i);
            if (i % bottomTitleSpacer == 0) {
                spacer.add(entry.getServerTime());
            }
        }
        return spacer;
    }
}
