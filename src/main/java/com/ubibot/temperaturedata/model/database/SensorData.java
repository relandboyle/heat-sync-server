package com.ubibot.temperaturedata.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity(name = "sensor_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorData implements Serializable {

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
    @JsonIgnore
    @JoinColumn(name = "unit_id")
    private UnitData unit;
}
