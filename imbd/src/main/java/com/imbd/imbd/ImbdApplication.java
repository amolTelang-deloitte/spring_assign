package com.imbd.imbd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
public class ImbdApplication {
	private static final Logger logger = LoggerFactory.getLogger(ImbdApplication.class);
	@Value("${server.port}")
	private int portNumber;

	public static void main(String[] args) {
		SpringApplication.run(ImbdApplication.class, args);
	}

	@PostConstruct
	public void logApplicationStartup() {
		logger.info("Started Imdb Application on port " + portNumber + " " + LocalDateTime.now());
	}

}
