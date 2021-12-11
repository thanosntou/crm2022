package com.ntouzidis.crm2022;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Crm2022Application {

	public static void main(String[] args) {
		SpringApplication.run(Crm2022Application.class, args);
	}

	public static final Logger logger = LoggerFactory.getLogger(Crm2022Application.class);

}
