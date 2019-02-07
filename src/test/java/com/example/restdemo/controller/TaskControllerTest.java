package com.example.restdemo.controller;

import com.example.restdemo.entity.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {

    @Mock
    private TaskRepository taskRepositoryMock;

    private TaskController taskController;

    private List<Task> tasks;

    private Task task1;

    @Before
    public void setUp(){

        task1 = new Task("task 1");

        tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(new Task("task 2"));
        tasks.add(new Task("task 3"));

        taskController = new TaskController();
        ReflectionTestUtils.setField(taskController, "taskRepository", taskRepositoryMock);
    }

    @Test
    public void when_taskList_then_allTasks() {

        when(taskRepositoryMock.findAll()).thenReturn(tasks);

        List<Task> result = taskController.taskList();

        assertEquals(result, tasks);

    }

    @Test
    public void given_slug_when_getTask_then_taskWithSlug() {

        final String SLUG1 = "task-1";

        when(taskRepositoryMock.findBySlug(SLUG1)).thenReturn(Optional.of(task1));

        Task result = taskController.getTask("task-1");

        assertEquals(result, task1);

    }

}