package ru.highloadcup.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.highloadcup.api.Location;
import ru.highloadcup.api.Visit;

import static ru.highloadcup.controller.VisitController.REST_PATH;

@RestController
@RequestMapping(REST_PATH)
public class VisitController {

    public static final String REST_PATH = "/visits";
    public static final String BY_ID = "/{id}";

    @RequestMapping(value = BY_ID, method = RequestMethod.GET)
    public Visit getVisit(@PathVariable Integer id) {
        Visit visit = new Visit();
        visit.setId(1);
        return visit;
    }
}
