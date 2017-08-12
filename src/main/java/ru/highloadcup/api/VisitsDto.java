package ru.highloadcup.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VisitsDto implements Serializable {

    private List<Visit> visits = new ArrayList<>();

    public VisitsDto() {
    }

    public VisitsDto(List<Visit> visits) {
        this.visits = visits;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }
}
