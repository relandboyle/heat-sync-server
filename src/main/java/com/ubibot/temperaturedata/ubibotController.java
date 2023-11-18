package com.ubibot.temperaturedata;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
public class ubibotController {

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
}
