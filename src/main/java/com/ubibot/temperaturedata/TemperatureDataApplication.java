package com.ubibot.temperaturedata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class TemperatureDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemperatureDataApplication.class, args);
	}

}
