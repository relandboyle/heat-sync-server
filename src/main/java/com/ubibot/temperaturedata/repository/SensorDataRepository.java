package com.ubibot.temperaturedata.repository;

import com.ubibot.temperaturedata.model.database.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, String> {

    List<SensorData> findByName(String name);

    List<SensorData> findByServerTimeBetween(String dateStart, String dateEnd);
}
