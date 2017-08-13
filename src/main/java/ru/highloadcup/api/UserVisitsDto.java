package ru.highloadcup.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserVisitsDto implements Serializable {

    private List<UserVisit> visits = new ArrayList<>();

    public UserVisitsDto() {
    }

    public UserVisitsDto(List<UserVisit> visits) {
        this.visits = visits;
    }

    public List<UserVisit> getVisits() {
        return visits;
    }

    public void setVisits(List<UserVisit> visits) {
        this.visits = visits;
    }
}
