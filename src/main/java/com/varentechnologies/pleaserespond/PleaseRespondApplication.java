package com.varentechnologies.pleaserespond;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PleaseRespondApplication {

	private static final Logger LOG = LoggerFactory.getLogger(PleaseRespondApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PleaseRespondApplication.class, args);
	}
}
