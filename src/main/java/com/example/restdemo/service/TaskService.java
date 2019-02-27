package com.example.restdemo.service;

import com.example.restdemo.dto.SearchTaskDto;
import com.example.restdemo.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskService {


    Page<Task> getAllTasks(Pageable pageable);

    Optional<Task> getTaskById(String id);

    Page<Task> getFiletredTasks(SearchTaskDto searchTaskDto, Pageable pageable);

    Task updateTask(Task task);

    void delete(String id);

    Task save(Task task);
}
