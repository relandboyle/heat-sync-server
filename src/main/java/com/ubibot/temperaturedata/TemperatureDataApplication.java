package com.ubibot.temperaturedata;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableConfigurationProperties(UbibotConfigProperties.class)
public class TemperatureDataApplication {

	private static Logger logger = LogManager.getLogger(TemperatureDataApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(TemperatureDataApplication.class, args);

		logger.debug("Debug log message");
		logger.info("Info log message");
		logger.error("Error log message");
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
