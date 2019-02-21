package com.example.restdemo.controller;

import com.example.restdemo.entity.Task;
import com.example.restdemo.exception.ResourceNotFoundException;
import com.example.restdemo.locator.ResourceLocator;
import com.example.restdemo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/task")
public class TaskController {

    final static ResourceNotFoundException TASK_NOT_FOUND_EXCEPTION = new ResourceNotFoundException("Task not Found");

    private static final int TASKS_PER_PAGE = 10;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired

    private ResourceLocator taskLocator;

    @GetMapping
    public Page<Task> taskList(@PageableDefault(size = TASKS_PER_PAGE) Pageable pageable) {

        return taskRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable("id") String id) {

        return taskRepository.findById(id)
                .orElseThrow(() -> TASK_NOT_FOUND_EXCEPTION);
    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity saveTask(@RequestBody Task task) {

        Task saved = taskRepository.save(task);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(taskLocator.getLocator(saved.getId()));

        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
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
