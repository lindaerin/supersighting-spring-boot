package com.supersighting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.supersight.controller", "com.supersight.dao.SightingDao"})
public class SupersightingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupersightingApplication.class, args);
	}

}
