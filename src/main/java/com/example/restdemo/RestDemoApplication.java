package com.example.restdemo;

import com.example.restdemo.controller.TaskRepository;
import com.example.restdemo.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private TaskRepository taskRepository;


	@Override
	public void run(String... args) throws Exception {

		System.out.println("App is up.");

		taskRepository.deleteAll();

		taskRepository.save(new Task("Feed the cat"));
		taskRepository.save(new Task("Feed the dog"));
		taskRepository.save(new Task("Feed the elephant"));
    }

}

