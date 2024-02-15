package com.ubibot.temperaturedata.service;

import com.ubibot.temperaturedata.model.weather.ForecastResponsePeriod;
import com.ubibot.temperaturedata.model.weather.NWSForecastResponse;
import com.ubibot.temperaturedata.model.weather.NWSGridResponse;
import com.ubibot.temperaturedata.model.weather.WeatherResponseProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Log4j2
@Service
public class WeatherServiceIntegrator {

    @Autowired
    RestTemplate restTemplate;

    public WeatherResponseProperties getNWSGridInfo(String latitude, String longitude) throws Exception {
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

    public ForecastResponsePeriod getNWSForecastInfo(String gridId, String gridX, String gridY) throws Exception {
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
}
