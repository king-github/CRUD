package com.example.restdemo.service;

import com.example.restdemo.dto.SearchTaskDto;
import com.example.restdemo.entity.Task;
import com.example.restdemo.exception.ResourceNotFoundException;
import com.example.restdemo.repository.TaskRepository;
import com.example.restdemo.util.SlugUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    @Qualifier("taskNotFoundException")
    private ResourceNotFoundException taskNotFoundException;

    @Autowired
    private TaskRepository taskRepository;


    @Override
    public Page<Task> getAllTasks(Pageable pageable) {

        return taskRepository.findAll(pageable);
    }

    @Override
    public Optional<Task> getTaskById(String id) {

        return taskRepository.findById(id);
    }

    @Override
    public Page<Task> getFiletredTasks(SearchTaskDto searchTaskDto, Pageable pageable) {

        return taskRepository.getFilteredTasks(searchTaskDto, pageable);
    }

    @Override
    public Task updateTask(Task updatedTask) {

        Task found = taskRepository.findById(updatedTask.getId())
                .orElseThrow(() -> taskNotFoundException);
        updateSlug(updatedTask);
        return taskRepository.save(updatedTask);
    }



    @Override
    public void delete(String id) {

        taskRepository.deleteById(id);
    }

    @Override
    public Task save(Task task) {

        task.setSlug( SlugUtil.slugify(task.getName()) );
        return taskRepository.save(task);
    }


    private void updateSlug(Task task){

        task.setSlug( SlugUtil.slugify(task.getName()) );
    }
}
