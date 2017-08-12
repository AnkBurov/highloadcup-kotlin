package ru.highloadcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.highloadcup.api.EmptyJson;
import ru.highloadcup.api.Visit;
import ru.highloadcup.dao.VisitDao;

import static ru.highloadcup.controller.VisitController.REST_PATH;
import static ru.highloadcup.util.HttpUtil.BY_ID;
import static ru.highloadcup.util.HttpUtil.JSON_HEADERS;
import static ru.highloadcup.util.HttpUtil.NEW;
import static ru.highloadcup.util.HttpUtil.NOT_FOUND;
import static ru.highloadcup.util.HttpUtil.OK;

@RestController
@RequestMapping(REST_PATH)
public class VisitController {

    public static final String REST_PATH = "/visits";

    @Autowired
    private VisitDao visitDao;

    @RequestMapping(value = NEW, method = RequestMethod.POST)
    public ResponseEntity<EmptyJson> createVisit(@RequestBody Visit visit) {
        visitDao.createVisit(visit);
        return OK;
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.POST)
    public ResponseEntity<EmptyJson> updateVisit(@PathVariable Integer id, @RequestBody Visit visit) {
        int numberOfUpdatedRecords = visitDao.updateVisit(id, visit);
        if (numberOfUpdatedRecords == 0) {
            return NOT_FOUND;
        }
        return OK;
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.GET)
    public ResponseEntity<Visit> getVisit(@PathVariable Integer id) {
        Visit visit = visitDao.getVisit(id);
        if (visit == null) {
            return NOT_FOUND;
        }
        return new ResponseEntity<>(visit, JSON_HEADERS, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAllExceptions(Exception e) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
