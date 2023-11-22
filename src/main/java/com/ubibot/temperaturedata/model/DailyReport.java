package com.ubibot.temperaturedata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyReport {

    @JsonProperty(value = "server_time")
    private String serverTime;

    @JsonValue
    List<SensorDataToPersist> channels;

}
