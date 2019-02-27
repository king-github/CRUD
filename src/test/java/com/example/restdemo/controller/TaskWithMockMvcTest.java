package com.example.restdemo.controller;

import com.example.restdemo.dto.SearchTaskDto;
import com.example.restdemo.entity.Task;
import com.example.restdemo.helper.CustomPageImpl;
import com.example.restdemo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private TaskService taskService;


    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private LocalDateTime dateTime;

    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private Task task5;

    private List<Task> allTasks;

    @Before
    public void setUp(){

        dateTime = LocalDateTime.parse("2019-01-12T12:25:17.123");

        task1 = new Task("Task1", dateTime.plusDays(0), dateTime.plusDays(10));
        task1.setId("id001");

        task2 = new Task("Task number two", dateTime.plusDays(1), dateTime.plusDays(11));
        task2.setId("id002");

        task3 = new Task("Task number three.", dateTime.plusDays(2), dateTime.plusDays(12));
        task3.setId("id003");

        task4 = new Task("Task number four.", dateTime.plusDays(3), dateTime.plusDays(13));
        task4.setId("id004");

        task5 = new Task("Task number five.", dateTime.plusDays(4), dateTime.plusDays(14));
        task5.setId("id005");

        allTasks = Arrays.asList(task1, task2, task3, task4, task5);
    }

    @Test
    public void shouldReturnListOfTasks() throws Exception {

        Page<Task> page = new CustomPageImpl<>(allTasks);
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("name").descending());

        when(taskService.getAllTasks(ArgumentMatchers.any(Pageable.class))).thenReturn(page);

        this.mockMvc.perform(get("/api/task"))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content", hasSize(allTasks.size())))

                .andExpect(jsonPath("$.content[0].id", is(task1.getId())))
                .andExpect(jsonPath("$.content[0].name", is(task1.getName())))
                .andExpect(jsonPath("$.content[0].slug", is(task1.getSlug())))
                .andExpect(jsonPath("$.content[0].startDate", is(task1.getStartDate().toString())))
                .andExpect(jsonPath("$.content[0].finishDate", is(task1.getFinishDate().toString())))

                .andExpect(jsonPath("$.content[1].id", is(task2.getId())))
                .andExpect(jsonPath("$.content[1].name", is(task2.getName())))
                .andExpect(jsonPath("$.content[1].slug", is(task2.getSlug())))
                .andExpect(jsonPath("$.content[1].startDate", is(task2.getStartDate().toString())))
                .andExpect(jsonPath("$.content[1].finishDate", is(task2.getFinishDate().toString())))

                .andExpect(jsonPath("$.content[2].id", is(task3.getId())))
                // ... skip
                .andExpect(jsonPath("$.content[3].id", is(task4.getId())))
                // ... skip

                .andExpect(jsonPath("$.content[4].id", is(task5.getId())))
                .andExpect(jsonPath("$.content[4].name", is(task5.getName())))
                .andExpect(jsonPath("$.content[4].slug", is(task5.getSlug())))
                .andExpect(jsonPath("$.content[4].startDate", is(task5.getStartDate().toString())))
                .andExpect(jsonPath("$.content[4].finishDate", is(task5.getFinishDate().toString())))
        ;
    }

    @Test
    public void shouldReturnTaskWithId() throws Exception {

        when(taskService.getTaskById(task2.getId())).thenReturn(Optional.of(task2));

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

        when(taskService.getTaskById(anyString())).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/api/task/" + "NoExistedId")).andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void shouldReturnFilteredTaskPage() throws Exception {

        SearchTaskDto searchTaskDto = new SearchTaskDto(
                                "task",
                                dateTime.plusDays(2),
                                dateTime.plusDays(4),
                                dateTime.plusDays(12),
                                dateTime.plusDays(14)
                );

        Page<Task> page = new CustomPageImpl<>(allTasks);
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("name").descending());

        when(taskService.getFiletredTasks(ArgumentMatchers.any(SearchTaskDto.class),
                                          ArgumentMatchers.any(Pageable.class)))
                                        .thenReturn(page);

        String searchTaskDtoJson = jacksonObjectMapper.writeValueAsString(searchTaskDto);

        this.mockMvc.perform(post("/api/task/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(searchTaskDtoJson)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content", hasSize(allTasks.size())))
        ;

    }



    @Test
    public void shouldAddNewTaskReturn201AndLocation() throws Exception {

        Task newTask = new Task("New task");
        newTask.setId("newID");

        String taskJson = jacksonObjectMapper.writeValueAsString(newTask);

        when(taskService.save(ArgumentMatchers.any(Task.class))).thenReturn(newTask);

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
        updated.setStartDate(dateTime.plusDays(2));
        updated.setFinishDate(dateTime.plusDays(4));

        String taskJson = jacksonObjectMapper.writeValueAsString(updated);

        when(taskService.updateTask(ArgumentMatchers.any(Task.class))).thenReturn(updated);

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