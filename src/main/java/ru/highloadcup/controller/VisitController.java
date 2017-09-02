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
import ru.highloadcup.api.VisitEO;
import ru.highloadcup.check.CustomChecker;
import ru.highloadcup.dao.VisitDao;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.Validator;

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

    @Autowired
    private CustomChecker<VisitEO> customChecker;

    @RequestMapping(value = NEW, method = RequestMethod.POST)
    public void createVisit(@RequestBody @Valid Visit visit, HttpServletRequest request) throws IOException {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.start(() -> {
            try {
                visitDao.createVisit(visit);
                createResponse(EmptyJson.INSTANCE, HttpStatus.OK, (HttpServletResponse) asyncContext.getResponse());
            } catch (Exception e) {
                try {
                    createResponse(HttpStatus.BAD_REQUEST, (HttpServletResponse) asyncContext.getResponse());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } finally {
                asyncContext.complete();
            }
        });
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.POST)
    public void updateVisit(@PathVariable Integer id, @RequestBody @Valid VisitEO visit,
                            HttpServletRequest request) throws IOException {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.start(() -> {
            try {
                customChecker.checkFullyNull(visit);
                int numberOfUpdatedRecords = visitDao.updateVisit(id, visit);
                if (numberOfUpdatedRecords == 0) {
                    createResponse(HttpStatus.NOT_FOUND, (HttpServletResponse) asyncContext.getResponse());
                }
                createResponse(EmptyJson.INSTANCE, HttpStatus.OK, (HttpServletResponse) asyncContext.getResponse());
            } catch (Exception e) {
                try {
                    createResponse(HttpStatus.BAD_REQUEST, (HttpServletResponse) asyncContext.getResponse());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } finally {
                asyncContext.complete();
            }
        });
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.GET)
    public void getVisit(@PathVariable Integer id, HttpServletRequest request) throws IOException {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.start(() -> {
            try {
                Visit visit = visitDao.getVisit(id);
                if (visit == null) {
                    createResponse(HttpStatus.NOT_FOUND, (HttpServletResponse) asyncContext.getResponse());
                }
                createResponse(visit, HttpStatus.OK, (HttpServletResponse) asyncContext.getResponse());
            } catch (Exception e) {
                try {
                    createResponse(HttpStatus.BAD_REQUEST, (HttpServletResponse) asyncContext.getResponse());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } finally {
                asyncContext.complete();
            }
        });
    }

    @ExceptionHandler(Exception.class)
    public void handleAllExceptions(Exception e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        if (e instanceof MethodArgumentTypeMismatchException) {
            createResponse(HttpStatus.NOT_FOUND, response);
        }
        createResponse(HttpStatus.BAD_REQUEST, response);
    }
}
