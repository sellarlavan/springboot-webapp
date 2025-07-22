package com.openclassrooms.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.webapp.model.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class WebappApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WebappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


	}
}
