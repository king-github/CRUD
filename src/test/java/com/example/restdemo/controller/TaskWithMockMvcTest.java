package com.example.restdemo.controller;

import com.example.restdemo.entity.Task;
import com.example.restdemo.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskWithMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private LocalDateTime dateTime1;
    private LocalDateTime dateTime2;

    private Task task1;
    private Task task2;
    private Task task3;

    private List<Task> allTasks;

    @Before
    public void setUp(){

        dateTime1 = LocalDateTime.parse("2019-01-12T12:25:17.123");
        dateTime2 = dateTime1.plusDays(2);

        task1 = new Task("Task1", dateTime1, dateTime2);
        task1.setId("id001");

        task2 = new Task("Task number two", dateTime1, dateTime2);
        task2.setId("id002");

        task3 = new Task("Task number three.", dateTime1, dateTime2);
        task3.setId("id003");

        allTasks = Arrays.asList(task1, task2, task3);
    }

    @Test
    public void shouldReturnListOfTasks() throws Exception {

        when(taskRepository.findAll()).thenReturn(allTasks);

        this.mockMvc.perform(get("/api/task"))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id", is(task1.getId())))
                .andExpect(jsonPath("$[0].name", is(task1.getName())))
                .andExpect(jsonPath("$[0].slug", is(task1.getSlug())))
                .andExpect(jsonPath("$[0].startDate", is(task1.getStartDate().toString())))
                .andExpect(jsonPath("$[0].finishDate", is(task1.getFinishDate().toString())))

                .andExpect(jsonPath("$[1].id", is(task2.getId())))
                .andExpect(jsonPath("$[1].name", is(task2.getName())))
                .andExpect(jsonPath("$[1].slug", is(task2.getSlug())))
                .andExpect(jsonPath("$[1].startDate", is(task2.getStartDate().toString())))
                .andExpect(jsonPath("$[1].finishDate", is(task2.getFinishDate().toString())))

                .andExpect(jsonPath("$[2].id", is(task3.getId())))
                .andExpect(jsonPath("$[2].name", is(task3.getName())))
                .andExpect(jsonPath("$[2].slug", is(task3.getSlug())))
                .andExpect(jsonPath("$[2].startDate", is(task3.getStartDate().toString())))
                .andExpect(jsonPath("$[2].finishDate", is(task3.getFinishDate().toString())))
        ;
    }

    @Test
    public void shouldReturnTaskWithId() throws Exception {

        when(taskRepository.findById(task2.getId())).thenReturn(Optional.of(task2));

        this.mockMvc.perform(get("/api/task/" +task2.getId()))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id", is(task2.getId())))
                .andExpect(jsonPath("$.name", is(task2.getName())))
                .andExpect(jsonPath("$.slug", is(task2.getSlug())))
                .andExpect(jsonPath("$.startDate", is(task2.getStartDate().toString())))
                .andExpect(jsonPath("$.finishDate", is(task2.getFinishDate().toString())))
        ;
    }

    @Test
    public void shouldReturnNotFound() throws Exception {

        when(taskRepository.findById(anyString())).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/api/task/" + "NoExistedId")).andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void shouldAddNewTaskReturn201AndLocation() throws Exception {

        Task newTask = new Task("New task");
        newTask.setId("newID");

        String taskJson = jacksonObjectMapper.writeValueAsString(newTask);

        when(taskRepository.save(ArgumentMatchers.any(Task.class))).thenReturn(newTask);

        this.mockMvc.perform(post("/api/task")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(taskJson)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string("location", endsWith("/api/task/newID")))
        ;
    }

    @Test
    public void shouldUpdateAndReturnTask() throws Exception {

        Task updated = new Task("Updated task");
        updated.setId(task2.getId());
        updated.setStartDate(dateTime2);
        updated.setFinishDate(dateTime1);

        String taskJson = jacksonObjectMapper.writeValueAsString(updated);

        when(taskRepository.findById(task2.getId())).thenReturn(Optional.of(task2));
        when(taskRepository.save(ArgumentMatchers.any(Task.class))).thenReturn(updated);

        this.mockMvc.perform(put("/api/task/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.id", is(updated.getId())))
                    .andExpect(jsonPath("$.name", is(updated.getName())))
                    .andExpect(jsonPath("$.slug", is(updated.getSlug())))
                    .andExpect(jsonPath("$.startDate", is(updated.getStartDate().toString())))
                    .andExpect(jsonPath("$.finishDate", is(updated.getFinishDate().toString())))
        ;
    }

    @Test
    public void shouldDeleteAndReturnOK() throws Exception {

        this.mockMvc.perform(delete("/api/task/" + "id"))
                    .andDo(print())
                    .andExpect(status().isOk())
        ;
    }

}