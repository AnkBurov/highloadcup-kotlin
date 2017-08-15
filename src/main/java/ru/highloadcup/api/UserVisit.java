package ru.highloadcup.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UserVisit implements Serializable {

    private Integer mark;

    @JsonProperty("visited_at")
    private Long visitedAt;

    private String place;

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Long getVisitedAt() {
        return visitedAt;
    }

    public void setVisitedAt(Long visitedAt) {
        this.visitedAt = visitedAt;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
