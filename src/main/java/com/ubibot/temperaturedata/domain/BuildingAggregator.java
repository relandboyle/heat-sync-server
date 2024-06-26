package com.ubibot.temperaturedata.domain;

import com.ubibot.temperaturedata.model.client.ClientBuildingRequest;
import com.ubibot.temperaturedata.model.database.BuildingData;
import com.ubibot.temperaturedata.service.BuildingIntegrator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class BuildingAggregator {

    @Autowired
    BuildingIntegrator integrator;

    public List<ClientBuildingRequest> searchForBuilding(ClientBuildingRequest request) {
        // building data returned from the database query
        var searchResult = integrator.searchForBuilding(request);

        // map from BuildingData to ClientBuildingRequest
        var mappedResult = searchResult.stream()
                .map(buildingData -> new ClientBuildingRequest(
                        buildingData.getId(),
                        buildingData.getStreetNumber(),
                        buildingData.getStreetName(),
                        buildingData.getCity(),
                        buildingData.getState(),
                        buildingData.getCountry(),
                        buildingData.getPostalCode(),
                        buildingData.getFullAddress()))
                .toList();
        log.info("BUILDING AGGREGATOR: STREAM CHECK: {}", mappedResult.get(0).getFullAddress());
        return mappedResult;
    }

    public String createBuilding(ClientBuildingRequest request) {
        var newBuilding = new BuildingData();
        if (request != null) {
            newBuilding.setId(request.getId());
            newBuilding.setStreetNumber(request.getStreetNumber());
            newBuilding.setStreetName(request.getStreetName());
            newBuilding.setCity(request.getCity());
            newBuilding.setState(request.getState());
            newBuilding.setPostalCode(request.getPostalCode());
            newBuilding.setCountry(request.getCountry());
            newBuilding.setFullAddress(
                    request.getStreetNumber() +
                    " " +
                    request.getStreetName() +
                    ", " +
                    request.getCity() +
                    ", " +
                    request.getState() +
                    ", " +
                    request.getPostalCode() +
                    ", " +
                    request.getCountry()
            );
        }

        return integrator.createBuilding(newBuilding);
    }
}
