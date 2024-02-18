package com.ubibot.temperaturedata.model.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientSensorResponse {

    private List<Long> bottomTitleSpacer;

    private List<ClientSensorData> sensorData;
}
