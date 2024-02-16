package com.ubibot.temperaturedata.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubibot.temperaturedata.UbibotConfigProperties;
import com.ubibot.temperaturedata.model.database.SensorData;
import com.ubibot.temperaturedata.model.ubibot.ChannelDataFromCloud;
import com.ubibot.temperaturedata.model.ubibot.ChannelListFromCloud;
import com.ubibot.temperaturedata.service.ScheduleIntegrator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log4j2
@Service
public class ScheduleAggregator {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeatherServiceAggregator weatherServiceAggregator;

    @Autowired
    private ScheduleIntegrator scheduleIntegrator;

    @Autowired
    private UbibotConfigProperties ubibotConfig;

    public void cronGetSensorDataAndPersist() throws Exception {
        String requestUrl = "";
        String webApiUrl = ubibotConfig.WEB_API_URL();
        String accountKey = ubibotConfig.ACCOUNT_KEY();
        try {
            requestUrl = String.valueOf(
                    new URI("https", webApiUrl, "/channels", "account_key=" + accountKey, null));
        } catch(URISyntaxException err) {
            log.error("An exception has occurred: {}", err.getMessage());
            throw new Exception(err);
        }

        // get the current data from all sensors on the account
        ChannelListFromCloud channelList = scheduleIntegrator.getChannelDataFromCloud(requestUrl);

        // map the response data to a list of simplified objects
        List<SensorData> sensorDataList = new ArrayList<>();
        try {
            sensorDataList = channelList != null ? mapChannelDataToSensorData(channelList) : null;
            assert sensorDataList != null;
        } catch(JsonProcessingException err) {
            log.error("An exception has occurred: {}", err.getMessage(), err);
        }

        // get the outside air temperature for each entry
        List<SensorData> sensorDataListWithOutsideAirTemps = weatherServiceAggregator.setOutsideAirTemperature(sensorDataList);
        // call a method to persist the prepared data to the database
        scheduleIntegrator.persistSensorData(sensorDataListWithOutsideAirTemps);
    }

    // takes sensor 'latest values' from the cloud and formats as SensorData objects
    public List<SensorData> mapChannelDataToSensorData(ChannelListFromCloud response) throws JsonProcessingException {
        // create a new list of SensorData to return
        List<SensorData> responseChannels = new ArrayList<>();

        // populate the responseChannels list
        // iterate over the list of sensor last values
        for (ChannelDataFromCloud chan : response.getChannels()) {
            System.out.println(System.currentTimeMillis());

            HashMap<?, ?> lastValues = objectMapper.readValue(chan.getLastValues(), HashMap.class);
            Object temperature = ((HashMap<?, ?>) lastValues.get("field1")).get("value");
            Object createdAt = ((HashMap<?, ?>) lastValues.get("field1")).get("created_at");
            log.info("CREATED AT: {} \n {}", createdAt, ZonedDateTime.parse(createdAt.toString()));

            SensorData channel = new SensorData();
            channel.setChannelId(chan.getChannelId());
            channel.setName(chan.getName());
            channel.setFieldOneLabel(chan.getFieldOneLabel());
            channel.setTemperature(temperature.toString());
            channel.setCreatedAt(ZonedDateTime.parse(createdAt.toString()));
            channel.setLatitude(chan.getLatitude());
            channel.setLongitude(chan.getLongitude());
            channel.setServerTime(response.getServerTime());
            responseChannels.add(0, channel);
        }
        return responseChannels;
    }
}
