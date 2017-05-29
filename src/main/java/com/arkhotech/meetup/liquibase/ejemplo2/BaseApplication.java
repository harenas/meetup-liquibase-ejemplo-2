package com.arkhotech.meetup.liquibase.ejemplo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.ws.rs.core.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@SpringBootApplication
public class BaseApplication {

	static final Logger logger = LogManager.getLogger(Application.class.getName());

	public static void main(String[] args) {
		logger.info("*************************************************");
		logger.info("*** EJEMPLO 2 - INGRESANDO A APLICACIÓN REST ****");
		logger.info("*************************************************");
		SpringApplication.run(BaseApplication.class, args);
	}
}
