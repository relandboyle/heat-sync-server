package com.ubibot.temperaturedata.repository;

import com.ubibot.temperaturedata.model.database.UnitData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitRepository extends JpaRepository<UnitData, String> {

    List<UnitData> findByFullUnitIgnoreCaseContaining(String fullUnit);
}

