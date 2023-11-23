package com.ubibot.temperaturedata.repository;

import com.ubibot.temperaturedata.model.database.UnitData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<UnitData, String> {
}
