package ru.highloadcup.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.Timestamp;

public class Visit implements Serializable {

    private Integer id;

    @JsonProperty("location")
    private Integer locationId;

    @JsonProperty("user")
    private Integer userId;

    @JsonProperty("visited_at")
    private Timestamp visitedAt;

    private Integer mark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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

        if (id != null ? !id.equals(visit.id) : visit.id != null) return false;
        if (locationId != null ? !locationId.equals(visit.locationId) : visit.locationId != null) return false;
        if (userId != null ? !userId.equals(visit.userId) : visit.userId != null) return false;
        if (visitedAt != null ? !visitedAt.equals(visit.visitedAt) : visit.visitedAt != null) return false;
        return mark != null ? mark.equals(visit.mark) : visit.mark == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (locationId != null ? locationId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (visitedAt != null ? visitedAt.hashCode() : 0);
        result = 31 * result + (mark != null ? mark.hashCode() : 0);
        return result;
    }
}
