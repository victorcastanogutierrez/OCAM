package com.ocam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class OcamApplication {

	public static void main(String[] args) {
		SpringApplication.run(OcamApplication.class, args);
	}
}
