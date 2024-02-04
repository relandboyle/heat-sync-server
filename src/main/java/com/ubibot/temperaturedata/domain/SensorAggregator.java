package com.ubibot.temperaturedata.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubibot.temperaturedata.UbibotConfigProperties;
import com.ubibot.temperaturedata.model.client.ClientSensorRequest;
import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.ubibot.ChannelDataFromCloud;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import com.ubibot.temperaturedata.model.weather.ForecastResponsePeriod;
import com.ubibot.temperaturedata.model.weather.NWSForecastResponse;
import com.ubibot.temperaturedata.model.weather.WeatherResponseProperties;
import com.ubibot.temperaturedata.model.weather.NWSGridResponse;
import com.ubibot.temperaturedata.repository.BuildingRepository;
import com.ubibot.temperaturedata.repository.SensorDataRepository;
import com.ubibot.temperaturedata.repository.UnitRepository;
import com.ubibot.temperaturedata.service.SensorIntegrator;
import lombok.extern.log4j.Log4j2;
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
    private UnitRepository unitRepository;

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    // return a list of sensor data entries based on user inputs
    public List<SensorData> getFilteredChannelData(ClientSensorRequest request) throws Exception {
        log.info("GET FILTERED CHANNEL DATA: {}", request);
        List<SensorData> response = null;

        // if a channelId AND a date range are provided
        // return data for only that unit, only within that date range
        if (request.getChannelId() != null && request.getDateRangeStart() != null && request.getDateRangeEnd() != null) {
            try {
                ZonedDateTime dateStart = request.getDateRangeStart();
                ZonedDateTime dateEnd =request.getDateRangeEnd();
                String channelId = request.getChannelId();
                log.info("NAME: {}\nDATES: {}, {}", channelId, dateStart, dateEnd);
                response = integrator.getFilteredChannelDataByIdAndDateRange(channelId, dateStart, dateEnd);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

         // if only a date range is provided
         // return all data from within the date range
        if (request.getDateRangeStart() != null && request.getDateRangeEnd() != null && request.getChannelId() == null) {
            try {
                ZonedDateTime dateStart = request.getDateRangeStart();
                ZonedDateTime dateEnd =request.getDateRangeEnd();
                log.info("DATES: {}, {}", dateStart, dateEnd);
                response = sensorDataRepository.findByServerTimeIsBetweenOrderByServerTimeDesc(dateStart, dateEnd);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

//         if only a channelId is provided
//         return all data for the selected unit
        if (request.getChannelId() != null && request.getDateRangeStart() == null || request.getDateRangeEnd() == null) {
            try {
                String channelId = request.getChannelId();
                log.info("CHANNEL ID: {}", channelId);
                response = sensorDataRepository.findByChannelIdOrderByServerTimeDesc(channelId);
            } catch(Exception err) {
                log.error("An exception was thrown: {}", err.getMessage());
            }
        }

        return response;
    }

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
        List<SensorData> sensorDataListWithOutsideAirTemps = setOutsideAirTemperature(sensorDataList);

        // call a method to persist the prepared data to the database
        integrator.persistSensorData(sensorDataListWithOutsideAirTemps);
    }

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

    private List<SensorData> setOutsideAirTemperature(List<SensorData> sensorDataList) throws Exception {
        for (SensorData entry : sensorDataList) {
            // latitude and longitude provided by Ubibot API
            String latitude = entry.getLatitude();
            String longitude = entry.getLongitude();

            // use lat/long to get National Weather Service grid coordinates
            WeatherResponseProperties gridInfo = new WeatherResponseProperties();
            try {
                gridInfo = getNWSGridInfo(latitude, longitude);
            } catch (Exception err) {
                log.error("An exception has occurred: {}", err.getMessage());
                throw new Exception(err);
            }

            // use NWS grid coordinates to get current outside air temp
            ForecastResponsePeriod forecast = new ForecastResponsePeriod();
            try {
                assert gridInfo != null;
                String gridId = gridInfo.getGridId();
                String gridX = gridInfo.getGridX();
                String gridY = gridInfo.getGridY();
                forecast = getNWSForecastInfo(gridId, gridX, gridY);
            } catch(Exception err) {
                log.error("An exception has occurred: {}", err.getMessage());
                throw new Exception(err);
            }

            // convert Fahrenheit to Celsius and set property on sensor entry
            assert forecast != null;
            double tempF = forecast.getTemperature();
            String tempC = convertFahrenheitToCelsius(tempF);
            entry.setOutsideTemperature(tempC);
        }

        return sensorDataList;
    }

    private WeatherResponseProperties getNWSGridInfo(String latitude, String longitude) throws Exception {
        String requestUrl = String.valueOf(new URI("https", "api.weather.gov", "/points/" + latitude + "," + longitude, null));
        log.info("REQUEST URL: {}", requestUrl);
        NWSGridResponse currentWeather = new NWSGridResponse();
        try {
            currentWeather = restTemplate.getForObject(requestUrl, NWSGridResponse.class);
        } catch (Exception err) {
            log.error("An exception has occurred: {}", err.getMessage());
            throw new Exception(err);
        }
        return currentWeather != null ? currentWeather.getProperties() : null;
    }

    private ForecastResponsePeriod getNWSForecastInfo(String gridId, String gridX, String gridY) throws Exception {
        String requestUrl = String.valueOf(new URI("https", "api.weather.gov","/gridpoints/" + gridId + "/" + gridX + "," + gridY + "/forecast", null));
        log.info("REQUEST URL: {}", requestUrl);
        NWSForecastResponse weatherForecast = new NWSForecastResponse();
        try {
            weatherForecast = restTemplate.getForObject(requestUrl, NWSForecastResponse.class);
        } catch (Exception err) {
            log.error("An exception has occurred: {}", err.getMessage());
            throw new Exception(err);
        }
        return weatherForecast != null ? weatherForecast.getProperties().getPeriods().get(0) : null;
    }
    private String convertFahrenheitToCelsius(double tempF) {
        double tempC = (tempF - 32) * 5 / 9;
        return String.format("%.2f", tempC);
    }

}
