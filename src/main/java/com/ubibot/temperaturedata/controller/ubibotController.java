package com.ubibot.temperaturedata.controller;

import com.ubibot.temperaturedata.model.DailyReport;
import com.ubibot.temperaturedata.service.QueryDataThirtyMinutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ubibotController {

    @Autowired
    QueryDataThirtyMinutes query;

    @GetMapping(value = "ubibot/dailyReport")
    String testGetMapping(@RequestHeader HttpHeaders headers) {
        System.out.println(headers.toString());
        return "Test is SUCCESSFUL!";
    }

    @PostMapping(value = "ubibot/dailyReport")
    String receiveDailyReport(@RequestBody DailyReport body, @RequestHeader HttpHeaders headers) {
        System.out.println(body);
        System.out.println(headers);

        return "SUCCESS";
    }

    @GetMapping(value = "cronTest")
    String testCronExpression() throws IOException {
        query.getChannelData();
        return "Called getChannelData()";
    }
}
