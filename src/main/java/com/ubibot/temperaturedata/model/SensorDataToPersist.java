package com.ubibot.temperaturedata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataToPersist {
    @Id
    @JsonProperty("sensor_id")
    private String sensorId;

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("server_time")
    private String serverTime;

    @JsonProperty("field1")
    private String fieldOneLabel;

    @JsonProperty("temperature")
    private String temperature;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private String unit;
}
