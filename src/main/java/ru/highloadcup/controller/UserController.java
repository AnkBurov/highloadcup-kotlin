package ru.highloadcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.highloadcup.api.EmptyJson;
import ru.highloadcup.api.User;
import ru.highloadcup.api.UsersDto;
import ru.highloadcup.api.Visit;
import ru.highloadcup.api.VisitsDto;
import ru.highloadcup.dao.UserDao;
import ru.highloadcup.dao.VisitDao;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static ru.highloadcup.controller.UserController.REST_PATH;
import static ru.highloadcup.util.HttpUtil.BY_ID;
import static ru.highloadcup.util.HttpUtil.JSON_HEADERS;
import static ru.highloadcup.util.HttpUtil.NEW;
import static ru.highloadcup.util.HttpUtil.NOT_FOUND;
import static ru.highloadcup.util.HttpUtil.OK;

@RestController
@RequestMapping(REST_PATH)
public class UserController {

    public static final String REST_PATH = "/users";

    @Autowired
    private UserDao userDao;

    @Autowired
    private VisitDao visitDao;

    @RequestMapping(method = RequestMethod.GET)
    public UsersDto getUsers() {
        User user = new User();
        user.setId(1);
        return new UsersDto(Collections.singletonList(user));
    }

    @RequestMapping(value = NEW, method = RequestMethod.POST)
    public ResponseEntity<EmptyJson> createUser(@RequestBody User user) {
        userDao.createUser(user);
        return OK;
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.POST)
    public ResponseEntity<EmptyJson> updateUser(@PathVariable Integer id, @RequestBody User user) {
        int numberOfUpdatedRecords = userDao.updateUser(id, user);
        if (numberOfUpdatedRecords == 0) {
            return NOT_FOUND;
        }
        return OK;
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        User user = userDao.getUser(id);
        if (user == null) {
            return NOT_FOUND;
        }
        return new ResponseEntity<>(user, JSON_HEADERS, HttpStatus.OK);
    }

    @RequestMapping(value = BY_ID + VisitController.REST_PATH, method = RequestMethod.GET)
    public ResponseEntity<VisitsDto> getVisits(@PathVariable Integer id,
                                               @RequestParam(required = false) Timestamp fromDate,
                                               @RequestParam(required = false) Timestamp toDate,
                                               @RequestParam(required = false) String country,
                                               @RequestParam(required = false) Integer toDistance) {
        User user = userDao.getUser(id);
        if (user == null) {
            return NOT_FOUND;
        }
        List<Visit> visits = visitDao.getVisits(id, fromDate, toDate, country, toDistance);
        return new ResponseEntity<>(new VisitsDto(visits), JSON_HEADERS, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAllExceptions(Exception e) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
