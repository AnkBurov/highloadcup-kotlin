package ru.highloadcup.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.highloadcup.api.Location;

import static ru.highloadcup.controller.LocationController.REST_PATH;

@RestController
@RequestMapping(REST_PATH)
public class LocationController {

    public static final String REST_PATH = "/locations";
    public static final String BY_ID = "/{id}";

    @RequestMapping(value = BY_ID, method = RequestMethod.GET)
    public Location getLocation(@PathVariable Integer id) {
        Location location = new Location();
        location.setId(1);
        return location;
    }
}
