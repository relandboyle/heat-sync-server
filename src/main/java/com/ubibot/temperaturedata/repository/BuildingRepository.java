package com.ubibot.temperaturedata.repository;

import com.ubibot.temperaturedata.model.database.BuildingData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<BuildingData, String> {
}
