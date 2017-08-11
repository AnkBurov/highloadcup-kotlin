package ru.highloadcup.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.highloadcup.api.User;
import ru.highloadcup.api.UsersDto;

import java.util.Collections;

import static ru.highloadcup.controller.UserController.REST_PATH;

@RestController
@RequestMapping(REST_PATH)
public class UserController {

    public static final String REST_PATH = "/users";
    public static final String BY_ID = "/{id}";

    @RequestMapping(method = RequestMethod.GET)
    public UsersDto getUsers() {
        User user = new User();
        user.setId(1);
        return new UsersDto(Collections.singletonList(user));
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.GET)
    public User getUser(@PathVariable Integer id) {
        User user = new User();
        user.setId(1);
        return user;
    }
}
