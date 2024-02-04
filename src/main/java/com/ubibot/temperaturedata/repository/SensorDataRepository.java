package com.ubibot.temperaturedata.repository;

import com.ubibot.temperaturedata.model.database.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, String> {

    List<SensorData> findByName(String name);

    List<SensorData> findByChannelIdOrderByServerTimeDesc(String channelId);

    List<SensorData> findByServerTimeIsBetweenOrderByServerTimeDesc(ZonedDateTime dateStart, ZonedDateTime dateEnd);

    List<SensorData> findByChannelIdAndServerTimeIsBetweenOrderByServerTimeAsc(String name, ZonedDateTime dateStart, ZonedDateTime dateEnd);
}
