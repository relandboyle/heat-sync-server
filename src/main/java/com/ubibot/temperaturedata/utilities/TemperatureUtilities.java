package com.ubibot.temperaturedata.utilities;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class TemperatureUtilities {

    public String convertFahrenheitToCelsius(double tempF) {
        double tempC = (tempF - 32) * 5 / 9;
        return String.format("%.2f", tempC);
    }
}
