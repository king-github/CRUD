package com.example.restdemo.controller;

import com.example.restdemo.entity.Task;
import com.example.restdemo.exception.ResourceNotFoundException;
import com.example.restdemo.locator.ResourceLocator;
import com.example.restdemo.repository.TaskRepository;
import com.example.restdemo.util.SlugUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)

public class TaskControllerTest {

    @Mock
    private TaskRepository taskRepositoryMock;

    @Mock
    private ResourceLocator taskLocatorMock;

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
        ReflectionTestUtils.setField(taskController, "taskLocator", taskLocatorMock);
    }

    @Test
    public void given_pager_when_taskList_then_pageOfTasks() {

        PageRequest pageRequest = PageRequest.of(2, 5);
        Page<Task> page = new PageImpl(tasks);

        when(taskRepositoryMock.findAll(pageRequest)).thenReturn(page);

        Page<Task> result = taskController.taskList(pageRequest);

        assertEquals(this.tasks, result.getContent());

    }

    @Test
    public void given_id_when_getTask_then_taskWithId() {

        final String ID1 = "id01";

        when(taskRepositoryMock.findById(ID1)).thenReturn(Optional.of(task1));

        Task result = taskController.getTask("id01");

        assertEquals(task1, result);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void given_noExistedSlug_when_getTask_then_throwException() {

        when(taskRepositoryMock.findById(any())).thenReturn(Optional.empty());

        Task result = taskController.getTask("no-existed-task");
    }

    @Test
    public void given_newTask_when_saveTask_then_save() throws URISyntaxException {

        String LOCATION = "http://9999/localhost/api/task/id123";

        when(taskRepositoryMock.save(task1)).thenReturn(task1);
        when(taskLocatorMock.getLocator(any())).thenReturn(new URI(LOCATION));

        URI location = taskController.saveTask(task1).getHeaders().getLocation();

        verify(taskRepositoryMock, times(1)).save(task1);
        assertEquals(LOCATION, location.toString());
    }


    @Test
    public void given_task_when_updateTask_then_save() {

        task1.setSlug("---");
        when(taskRepositoryMock.save(task1)).thenReturn(task1);
        when(taskRepositoryMock.findById(task1.getId())).thenReturn(Optional.of(task1));

        Task result = taskController.updateTask(task1);

        verify(taskRepositoryMock, times(1)).save(task1);
        assertEquals(task1, result);
        assertEquals(task1.getSlug(), SlugUtil.slugify(task1.getName()));
    }

    @Test
    public void given_taskId_when_deleteTask_then_deleteTaskWithId() {

        taskController.deleteTask("id1");
        verify(taskRepositoryMock, times(1)).deleteById("id1");
    }

}