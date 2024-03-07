package com.ubibot.temperaturedata.repository;

import com.ubibot.temperaturedata.model.database.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public interface SensorDataRepository extends JpaRepository<SensorData, String> {

    ArrayList<SensorData> findByChannelIdOrderByServerTimeAsc(String channelId);

    ArrayList<SensorData> findByServerTimeIsBetweenOrderByServerTimeAsc(ZonedDateTime dateStart, ZonedDateTime dateEnd);

    ArrayList<SensorData> findByChannelIdAndServerTimeIsBetweenOrderByServerTimeAsc(String name, ZonedDateTime dateStart, ZonedDateTime dateEnd);
}
