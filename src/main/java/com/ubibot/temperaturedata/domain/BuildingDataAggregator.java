package com.ubibot.temperaturedata.domain;

import com.ubibot.temperaturedata.model.database.BuildingData;
import com.ubibot.temperaturedata.service.BuildingDataIntegrator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BuildingDataAggregator {

    @Autowired
    BuildingDataIntegrator integrator;

    public String createBuilding(BuildingData newBuilding) {
        return integrator.createBuilding(newBuilding);
    }

}
