package com.example.restdemo.repository;

import com.example.restdemo.dto.SearchTaskDto;
import com.example.restdemo.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CustomTask {
    abstract Page<Task> getFilteredTasks(SearchTaskDto searchTaskDto, Pageable pageable);

    List<Task> getFilteredTasks(SearchTaskDto searchTaskDto, Sort sort);

    List<Task> getFilteredTasks(SearchTaskDto searchTaskDto);
}
