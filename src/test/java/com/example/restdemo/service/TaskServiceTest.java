package com.example.restdemo.service;

import com.example.restdemo.dto.SearchTaskDto;
import com.example.restdemo.entity.Task;
import com.example.restdemo.exception.ResourceNotFoundException;
import com.example.restdemo.repository.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepositoryMock;

    @Mock
    private ResourceNotFoundException taskNotfoundExceptionMock;

    private TaskService taskService;

    @Before
    public void setUp() {

        taskService = new TaskServiceImpl();
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepositoryMock);
        ReflectionTestUtils.setField(taskService, "taskNotFoundException", taskNotfoundExceptionMock);

                //new ResourceNotFoundException("Not found"));
    }

    @Test
    public void shouldCallGetAllTasks(){

        taskService.getAllTasks(Pageable.unpaged());
        verify(taskRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void shouldCallFindById(){

        taskService.getTaskById("id");
        verify(taskRepositoryMock, times(1)).findById("id");
    }

    @Test
    public void shouldCallgetFilterdTasks(){

        taskService.getFiletredTasks(new SearchTaskDto(), Pageable.unpaged());
        verify(taskRepositoryMock, times(1)).getFilteredTasks(any(), any(Pageable.class));
    }

    @Test
    public void shouldCallUpdateTask(){

        Task task = new Task("task");
        task.setId("id001");
        Task updated = new Task("updated");
        updated.setId("id001");

        when(taskRepositoryMock.findById("id001")).thenReturn(Optional.of(task));

        Task result = taskService.updateTask(updated);

        verify(taskRepositoryMock, times(1)).findById("id001");
        verify(taskRepositoryMock, times(1)).save(any(Task.class));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWhenTaskWithIdNoExist(){

        Task task = new Task("Task");
        task.setId("NoExisted");

        when(taskRepositoryMock.findById(any())).thenReturn(Optional.empty());
        taskService.updateTask(task);

    }

    @Test
    public void shouldCallSaveTask(){

        Task task = new Task("Task1");

        taskService.save(task);
        verify(taskRepositoryMock, times(1)).save(task);
    }

    @Test
    public void shouldCallDeleteTaskWithId(){

        taskService.delete("id001");
        verify(taskRepositoryMock, times(1)).deleteById("id001");
    }
}
