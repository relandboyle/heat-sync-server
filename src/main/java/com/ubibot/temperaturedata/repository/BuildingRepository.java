package com.ubibot.temperaturedata.repository;

import com.ubibot.temperaturedata.model.database.BuildingData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingRepository extends JpaRepository<BuildingData, String> {

    List<BuildingData> findByStreetNumberContainingOrStreetNameContainingOrPostalCodeContaining(String streetNumber, String streetName, String postalCode);
}
