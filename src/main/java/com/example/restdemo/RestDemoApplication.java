package com.example.restdemo;


import com.example.restdemo.entity.Task;
import com.example.restdemo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableMongoAuditing
public class RestDemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}

	@Autowired
	private TaskRepository taskRepository;


	@Autowired
	private MongoTemplate mongoTemplate;

	private Task createTask(String name, long days){

		LocalDateTime now = LocalDateTime.now();

		return new Task(name, now, now.plusDays(days));
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("App is up.");

		taskRepository.deleteAll();

		taskRepository.save(createTask("Feed the cat", 2));
		taskRepository.save(createTask("Feed the dog", 3));
		taskRepository.save(createTask("Feep the elephant", 3));
		taskRepository.save(createTask("Feed the butterfly", 1));
		taskRepository.save(createTask("Feep the fly", 5));

    }

}

