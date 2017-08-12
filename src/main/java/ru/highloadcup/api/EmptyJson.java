package ru.highloadcup.api;

import java.io.Serializable;

public class EmptyJson implements Serializable {

    public static final EmptyJson INSTANCE = new EmptyJson();

    private Integer dummyId;

    public Integer getDummyId() {
        return dummyId;
    }

    public void setDummyId(Integer dummyId) {
        this.dummyId = dummyId;
    }
}
