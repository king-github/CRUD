package com.example.restdemo.entity;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Task {

    @Id
    private String id;

    private String name;

    private String slug;

    private LocalDateTime startDate;

    private LocalDateTime finishDate;



    public Task() {
        this.setStartDate(LocalDateTime.now());
    }

    public Task(String name) {
        this(name, LocalDateTime.now(), null);
    }

    public Task(String name, LocalDateTime startDate, LocalDateTime finishDate) {
        this.setName(name);
        this.setStartDate(startDate);
        this.setFinishDate(finishDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }
}
