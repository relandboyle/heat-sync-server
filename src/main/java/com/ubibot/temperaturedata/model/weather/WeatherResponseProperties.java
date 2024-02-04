package com.ubibot.temperaturedata.model.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseProperties {

    @JsonValue
    private String gridId;

    @JsonValue
    private String gridX;

    @JsonValue
    private String gridY;

    @JsonValue
    private List<ForecastResponsePeriod> periods;
}
