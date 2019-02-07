package com.example.restdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class RestDemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		System.out.println("App is up.");

    }

}

