package com.example.restdemo.controller;

import com.example.restdemo.entity.Task;
import com.example.restdemo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskController {

    final static ResourceNotFoundException TASK_NOT_FOUND_EXCEPTION = new ResourceNotFoundException("Task not Found");

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> taskList() {

        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable("id") String id) {

        return taskRepository.findById(id)
                .orElseThrow(() -> TASK_NOT_FOUND_EXCEPTION);
    }

    @PostMapping
    public Task saveTask(@RequestBody Task task) {

        Task saved = taskRepository.save(task);

        return saved;
    }

    @PutMapping
    public Task updateTask(@RequestBody Task updatedTask) {

        Task found = taskRepository.findById(updatedTask.getId())
                .orElseThrow(() -> TASK_NOT_FOUND_EXCEPTION);
        updatedTask.updateSlug();
        Task saved = taskRepository.save(updatedTask);

        return saved;
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") String id) {

        taskRepository.deleteById(id);
    }
}
