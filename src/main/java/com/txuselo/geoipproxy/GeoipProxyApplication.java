package com.txuselo.geoipproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
// @EnableConfigurationProperties(IpapiConfig.class)
public class GeoipProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoipProxyApplication.class, args);
	}

}
