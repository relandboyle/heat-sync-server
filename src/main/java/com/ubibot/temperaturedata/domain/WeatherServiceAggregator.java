package com.ubibot.temperaturedata.domain;

import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.weather.ForecastResponsePeriod;
import com.ubibot.temperaturedata.model.weather.WeatherResponseProperties;
import com.ubibot.temperaturedata.service.WeatherServiceIntegrator;
import com.ubibot.temperaturedata.utilities.TemperatureUtilities;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class WeatherServiceAggregator {

    @Autowired
    WeatherServiceIntegrator nwsIntegrator;

    @Autowired
    TemperatureUtilities tempUtilities;

    public List<SensorData> setOutsideAirTemperature(List<SensorData> sensorDataList) throws Exception {
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

            // convert Fahrenheit to Celsius string and set property on sensor entry
            double tempF = forecast.getTemperature();
            String tempC = tempUtilities.convertFahrenheitToCelsius(tempF);
            entry.setOutsideTemperature(tempC);
        }

        return sensorDataList;
    }

    public WeatherResponseProperties getNWSGridInfo(String latitude, String longitude) throws Exception {
        return nwsIntegrator.getNWSGridInfo(latitude, longitude);
    }

    public ForecastResponsePeriod getNWSForecastInfo(String gridId, String gridX, String gridY) throws Exception {
        return nwsIntegrator.getNWSForecastInfo(gridId, gridX, gridY);
    }
}
