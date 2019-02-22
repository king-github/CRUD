package com.example.restdemo.repository;

import com.example.restdemo.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface TaskRepository extends MongoRepository<Task, String>, CustomTask {

    Optional<Task> findBySlug(String slug);



}
