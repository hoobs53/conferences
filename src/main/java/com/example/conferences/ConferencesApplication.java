package com.example.conferences;

import com.example.conferences.model.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConferencesApplication {
	@Autowired
	Conference conference;

	public static void main(String[] args) {
		SpringApplication.run(ConferencesApplication.class, args);
	}

}
