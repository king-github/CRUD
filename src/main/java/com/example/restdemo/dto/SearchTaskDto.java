package com.example.restdemo.dto;

import java.time.LocalDateTime;

public class SearchTaskDto {

    private String likeName;

    private LocalDateTime fromStartDate;
    private LocalDateTime toStartDate;

    private LocalDateTime fromFinishDate;
    private LocalDateTime toFinishDate;

    public SearchTaskDto() {
    }

    public SearchTaskDto(String likeName, LocalDateTime fromStartDate, LocalDateTime toStartDate, LocalDateTime fromFinishDate, LocalDateTime toFinishDate) {
        this.likeName = likeName;
        this.fromStartDate = fromStartDate;
        this.toStartDate = toStartDate;
        this.fromFinishDate = fromFinishDate;
        this.toFinishDate = toFinishDate;
    }

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public LocalDateTime getFromStartDate() {
        return fromStartDate;
    }

    public void setFromStartDate(LocalDateTime fromStartDate) {
        this.fromStartDate = fromStartDate;
    }

    public LocalDateTime getToStartDate() {
        return toStartDate;
    }

    public void setToStartDate(LocalDateTime toStartDate) {
        this.toStartDate = toStartDate;
    }

    public LocalDateTime getFromFinishDate() {
        return fromFinishDate;
    }

    public void setFromFinishDate(LocalDateTime fromFinishDate) {
        this.fromFinishDate = fromFinishDate;
    }

    public LocalDateTime getToFinishDate() {
        return toFinishDate;
    }

    public void setToFinishDate(LocalDateTime toFinishDate) {
        this.toFinishDate = toFinishDate;
    }
}
