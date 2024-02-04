package com.ubibot.temperaturedata.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping
    String whiteLabel() {
        return "<html>" +
                    "<body>" +
                        "<br><br>" +
                        "You have reached the HeatSync server!" +
                        "<br><br>" +
                        "Please visit <a href='https://heat-sync.net'>HeatSync.net</a> to access the site." +
                    "</body>" +
                "</html>";
    }
}
