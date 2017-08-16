package ru.highloadcup.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Visit implements Serializable {

    @NotNull
    private Integer id;

    @NotNull
    @JsonProperty("location")
    private Integer locationId;

    @NotNull
    @JsonProperty("user")
    private Integer userId;

    @NotNull
    @Min(946684800L)
    @Max(1420070400L)
    @JsonProperty("visited_at")
    private Long visitedAt;

    @NotNull
    @Min(0)
    @Max(5)
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

    public Long getVisitedAt() {
        return visitedAt;
    }

    public void setVisitedAt(Long visitedAt) {
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
