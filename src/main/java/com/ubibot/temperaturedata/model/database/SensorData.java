package com.ubibot.temperaturedata.model.database;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity(name = "sensor_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("entry_id")
    private String entryId;

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("server_time")
    private ZonedDateTime serverTime;

    @JsonProperty("field1")
    private String fieldOneLabel;

    @JsonProperty("temperature")
    private String temperature;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("longitude")
    private String longitude;

    @JsonProperty("outside_temperature")
    private String outsideTemperature;
}
