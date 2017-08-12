package ru.highloadcup.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LocationsDto implements Serializable {

    private List<Location> locations = new ArrayList<>();

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
