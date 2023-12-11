package com.ubibot.temperaturedata.domain;

import com.ubibot.temperaturedata.model.client.ClientBuildingRequest;
import com.ubibot.temperaturedata.model.database.BuildingData;
import com.ubibot.temperaturedata.service.BuildingDataIntegrator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class BuildingDataAggregator {

    @Autowired
    BuildingDataIntegrator integrator;

    public List<BuildingData> searchForBuilding(ClientBuildingRequest request) {
        return integrator.searchForBuilding(request);
    }

    public String createBuilding(BuildingData newBuilding) {
        return integrator.createBuilding(newBuilding);
    }

}
