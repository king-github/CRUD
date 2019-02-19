package com.example.restdemo.controller;

import com.example.restdemo.entity.Task;
import com.example.restdemo.repository.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestRestTemplate restTemplate;


    private List<Task> allTasks;

    @Before
    public void setUp() {

        allTasks = taskRepository.findAll();
    }

    @Test
    public void shouldReturnAllTasks() {

        ResponseEntity<List<Task>> responseEntity = this.restTemplate
                .exchange("http://localhost:" + port + "/api/task",
                            HttpMethod.GET,
                null,
                            new ParameterizedTypeReference<List<Task>>() {});

        List<Task> actual = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertEquals(actual.size(), allTasks.size());
    }

    @Test
    public void shouldReturnNotFound() {

        ResponseEntity<String> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/api/task/no-exist-id",
                                        String.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(responseEntity.getBody().toLowerCase(), containsString("not found"));
    }

    @Test
    public void shouldReturnOneTask() {

        ResponseEntity<Task> responseEntity = this.restTemplate
                .exchange("http://localhost:" + port + "/api/task/" + allTasks.get(0).getId(),
                        HttpMethod.GET,
                        null,
                        Task.class);

        Task actual = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(actual, samePropertyValuesAs(allTasks.get(0)));

    }

    @Test
    public void shouldAddNewTaskReturn201AndLocation() {

        HttpEntity<Task> newTask = new HttpEntity<>(new Task("New task"));

        ResponseEntity<Task> responseEntity = this.restTemplate
                .exchange("http://localhost:" + port + "/api/task",
                        HttpMethod.POST,
                        newTask,
                        Task.class);

        Task actual = responseEntity.getBody();

        String[] parts = responseEntity.getHeaders().getLocation().getPath().split("/");

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertEquals(responseEntity.getHeaders().getLocation().toString(),
                "http://localhost:" + port + "/api/task/" + parts[parts.length-1]);

    }

    @Test
    public void shouldUpdateTaskAndReturnIt() throws Exception {

        Task updatedTask = new Task("Updated task");
        updatedTask.setId(allTasks.get(0).getId());
        updatedTask.setStartDate(LocalDateTime.now());
        updatedTask.setFinishDate(LocalDateTime.now().plusDays(23));

        HttpEntity<Task> updatedEntityTask = new HttpEntity<>(updatedTask);

        ResponseEntity<Task> responseEntity = this.restTemplate
                .exchange("http://localhost:" + port + "/api/task",
                        HttpMethod.PUT,
                        updatedEntityTask,
                        Task.class);

        Task actual = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(actual, samePropertyValuesAs(updatedTask));
    }

    @Test
    public void shouldDeleteTask() {

        String taskId = allTasks.get(1).getId();

        ResponseEntity<String> responseEntity =
                this.restTemplate.exchange("http://localhost:" + port + "/api/task/{taskId}",
                                        HttpMethod.DELETE,
                            null,
                                        String.class,
                                        taskId);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
    }

}