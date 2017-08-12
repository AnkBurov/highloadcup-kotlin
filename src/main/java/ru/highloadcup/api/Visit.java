package ru.highloadcup.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.Timestamp;

public class Visit implements Serializable {

    private int id;

    @JsonProperty("location")
    private int locationId;

    @JsonProperty("user")
    private int userId;

    @JsonProperty("visitedAt")
    private Timestamp visitedAt;

    private Integer mark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getVisitedAt() {
        return visitedAt;
    }

    public void setVisitedAt(Timestamp visitedAt) {
        this.visitedAt = visitedAt;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Visit visit = (Visit) o;

        if (id != visit.id) return false;
        if (locationId != visit.locationId) return false;
        if (userId != visit.userId) return false;
        if (visitedAt != null ? !visitedAt.equals(visit.visitedAt) : visit.visitedAt != null) return false;
        return mark != null ? mark.equals(visit.mark) : visit.mark == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + locationId;
        result = 31 * result + userId;
        result = 31 * result + (visitedAt != null ? visitedAt.hashCode() : 0);
        result = 31 * result + (mark != null ? mark.hashCode() : 0);
        return result;
    }
}
