package com.ubibot.temperaturedata.model.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "sensor_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private UnitData unit;
}
