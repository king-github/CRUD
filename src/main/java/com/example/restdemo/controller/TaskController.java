package com.example.restdemo.controller;

import com.example.restdemo.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskController {


    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> taskList() {

        return taskRepository.findAll();
    }

    @GetMapping("/{slug}")
    public Task getTask(@PathVariable("slug") String slug) {

        return taskRepository.findBySlug(slug).orElseThrow(RuntimeException::new);
    }



}
