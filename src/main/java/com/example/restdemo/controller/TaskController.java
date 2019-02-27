package com.example.restdemo.controller;

import com.example.restdemo.dto.SearchTaskDto;
import com.example.restdemo.entity.Task;
import com.example.restdemo.exception.ResourceNotFoundException;
import com.example.restdemo.locator.ResourceLocator;
import com.example.restdemo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier("taskNotFoundException")
    private ResourceNotFoundException taskNotFoundException;

    private static final int TASKS_PER_PAGE = 10;

    @Autowired
    private TaskService taskService;

    @Autowired

    private ResourceLocator taskLocator;

    @GetMapping
    public Page<Task> taskList(@PageableDefault(size = TASKS_PER_PAGE) Pageable pageable) {

        return taskService.getAllTasks(pageable);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable("id") String id) {

        return taskService.getTaskById(id)
                .orElseThrow(() -> taskNotFoundException);
    }

    @PostMapping("search")
    public Page<Task> searchTask(@PageableDefault(size = TASKS_PER_PAGE) Pageable pageable,
                                 @RequestBody SearchTaskDto searchTaskDto) {

        return taskService.getFiletredTasks(searchTaskDto, pageable);

    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity saveTask(@RequestBody Task task) {

        Task saved = taskService.save(task);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(taskLocator.getLocator(saved.getId()));

        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping
    public Task updateTask(@RequestBody Task updatedTask) {

        return taskService.updateTask(updatedTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") String id) {

        taskService.delete(id);
    }

}
