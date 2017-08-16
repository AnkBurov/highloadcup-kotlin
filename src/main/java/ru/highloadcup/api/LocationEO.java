package ru.highloadcup.api;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

public class LocationEO {

    private String place;

    @Size(max = 50)
    private String country;

    @Size(max = 50)
    private String city;

    private Integer distance;

    @JsonCreator
    public LocationEO(Map<String, Object> props) {
        for (Object value : props.values()) {
            if (value == null) throw new IllegalArgumentException();
        }
        this.place = (String) props.get("place");
        this.country = (String) props.get("country");
        this.city = (String) props.get("city");
        this.distance = (Integer) props.get("distance");
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
