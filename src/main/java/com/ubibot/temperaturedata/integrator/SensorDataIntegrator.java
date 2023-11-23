package com.ubibot.temperaturedata.integrator;

import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.database.UnitData;
import com.ubibot.temperaturedata.repository.SensorDataRepository;
import com.ubibot.temperaturedata.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorDataIntegrator {

    @Autowired
    SensorDataRepository sensorDataRepository;

    @Autowired
    UnitRepository unitRepository;

    public String persistSensorData(List<SensorData> channelData) {
        System.out.println("HIT INTEGRATOR: " + channelData);
        for (SensorData channel : channelData) {
            System.out.println("CHANNEL IN LOOP: " + channel);
            String sensorName = channel.getName();
//            System.out.println(channel);
//            System.out.println(sensorName);
            Optional<UnitData> unit = unitRepository.findById(sensorName);
            channel.setUnit(unit.get());
        }
        sensorDataRepository.saveAll(channelData);
        return "ENTRY OR ENTRIES WERE ADDED TO THE SENSOR DATA TABLE";
    }
}
