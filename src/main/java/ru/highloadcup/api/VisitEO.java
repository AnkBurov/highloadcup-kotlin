package ru.highloadcup.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class VisitEO {

    @JsonProperty("location")
    private Integer locationId;

    @JsonProperty("user")
    private Integer userId;

    @Min(946684800L)
    @Max(1420070400L)
    @JsonProperty("visited_at")
    private Long visitedAt;

    @Min(0)
    @Max(5)
    private Integer mark;

    @JsonCreator
    public VisitEO(Map<String, Object> props) {
        for (Object value : props.values()) {
            if (value == null) throw new IllegalArgumentException();
        }
        this.locationId = (Integer) props.get("location");
        this.userId = (Integer) props.get("user");
        Object visited_at = props.get("visited_at");
        if (visited_at != null) {
            this.visitedAt = Long.valueOf(String.valueOf(visited_at));
        }
        this.mark = (Integer) props.get("mark");
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
}
