package com.ubibot.temperaturedata;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ubibot")
public record UbibotConfigProperties(String ACCOUNT_KEY, String WEB_API_URL) {

}
