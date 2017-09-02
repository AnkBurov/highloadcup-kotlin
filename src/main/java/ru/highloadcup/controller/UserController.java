package ru.highloadcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.highloadcup.api.EmptyJson;
import ru.highloadcup.api.User;
import ru.highloadcup.api.UserEO;
import ru.highloadcup.api.UserVisit;
import ru.highloadcup.api.UserVisitsDto;
import ru.highloadcup.api.UsersDto;
import ru.highloadcup.check.CustomChecker;
import ru.highloadcup.dao.UserDao;
import ru.highloadcup.dao.VisitDao;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static ru.highloadcup.controller.UserController.REST_PATH;
import static ru.highloadcup.util.HttpUtil.BY_ID;
import static ru.highloadcup.util.HttpUtil.NEW;
import static ru.highloadcup.util.HttpUtil.createResponse;

@RestController
@RequestMapping(REST_PATH)
public class UserController {

    public static final String REST_PATH = "/users";

    @Autowired
    private UserDao userDao;

    @Autowired
    private VisitDao visitDao;

    @Autowired
    @Qualifier("userEOCustomChecker")
    private CustomChecker<UserEO> customChecker;

    @RequestMapping(method = RequestMethod.GET)
    public UsersDto getUsers() {
        User user = new User();
        user.setId(1);
        return new UsersDto(Collections.singletonList(user));
    }

    @RequestMapping(value = NEW, method = RequestMethod.POST)
    public void createUser(@RequestBody @Valid User user, HttpServletRequest request) throws IOException {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.start(() -> {
            try {
                userDao.createUser(user);
                createResponse(EmptyJson.Companion.getINSTANCE(), HttpStatus.OK, (HttpServletResponse) asyncContext.getResponse());
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
    public void updateUser(@PathVariable Integer id, @RequestBody @Valid UserEO user, HttpServletRequest request) throws IOException {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.start(() -> {
            try {
                customChecker.checkFullyNull(user);
                int numberOfUpdatedRecords = userDao.updateUser(id, user);
                if (numberOfUpdatedRecords == 0) {
                    createResponse(HttpStatus.NOT_FOUND, (HttpServletResponse) asyncContext.getResponse());
                }
                createResponse(EmptyJson.Companion.getINSTANCE(), HttpStatus.OK, (HttpServletResponse) asyncContext.getResponse());
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
    public void getUser(@PathVariable Integer id, HttpServletRequest request) throws IOException {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.start(() -> {
            try {
                User user = userDao.getUser(id);
                if (user == null) {
                    createResponse(HttpStatus.NOT_FOUND, (HttpServletResponse) asyncContext.getResponse());
                }
                createResponse(user, HttpStatus.OK, (HttpServletResponse) asyncContext.getResponse());
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

    @RequestMapping(value = BY_ID + VisitController.REST_PATH, method = RequestMethod.GET)
    public void getUserVisits(@PathVariable Integer id,
                              HttpServletRequest request,
                              @RequestParam(required = false) String fromDate,
                              @RequestParam(required = false) String toDate,
                              @RequestParam(required = false) String country,
                              @RequestParam(required = false) String toDistance) throws IOException {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.start(() -> {
            try {
                User user = userDao.getUser(id);
                if (user == null) {
                    createResponse(HttpStatus.NOT_FOUND, (HttpServletResponse) asyncContext.getResponse());
                }
                List<UserVisit> userVisits = visitDao.getUserVisits(id, fromDate, toDate, country, toDistance);
                createResponse(new UserVisitsDto(userVisits), HttpStatus.OK, (HttpServletResponse) asyncContext.getResponse());
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
