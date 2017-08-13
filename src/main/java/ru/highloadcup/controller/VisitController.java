package ru.highloadcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.highloadcup.api.EmptyJson;
import ru.highloadcup.api.Visit;
import ru.highloadcup.dao.VisitDao;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static ru.highloadcup.controller.VisitController.REST_PATH;
import static ru.highloadcup.util.HttpUtil.BY_ID;
import static ru.highloadcup.util.HttpUtil.NEW;
import static ru.highloadcup.util.HttpUtil.createResponse;

@RestController
@RequestMapping(REST_PATH)
public class VisitController {

    public static final String REST_PATH = "/visits";

    @Autowired
    private VisitDao visitDao;

    @RequestMapping(value = NEW, method = RequestMethod.POST)
    public void createVisit(@RequestBody Visit visit, HttpServletResponse response) throws IOException {
        visitDao.createVisit(visit);
        createResponse(EmptyJson.INSTANCE, HttpStatus.OK, response);
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.POST)
    public void updateVisit(@PathVariable Integer id, @RequestBody Visit visit, HttpServletResponse response) throws IOException {
        int numberOfUpdatedRecords = visitDao.updateVisit(id, visit);
        if (numberOfUpdatedRecords == 0) {
            createResponse(HttpStatus.NOT_FOUND, response);
        }
        createResponse(EmptyJson.INSTANCE, HttpStatus.OK, response);
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.GET)
    public void getVisit(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Visit visit = visitDao.getVisit(id);
        if (visit == null) {
            createResponse(HttpStatus.NOT_FOUND, response);
        }
        createResponse(visit, HttpStatus.OK, response);
    }

    @ExceptionHandler(Exception.class)
    public void handleAllExceptions(Exception e, HttpServletResponse response) throws IOException {
        if (e instanceof MethodArgumentTypeMismatchException) {
            createResponse(HttpStatus.NOT_FOUND, response);
        }
        createResponse(HttpStatus.BAD_REQUEST, response);
    }
}
