package ru.highloadcup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.highloadcup.api.User;
import ru.highloadcup.api.UsersDto;
import ru.highloadcup.controller.UserController;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getUsers() throws Exception {
        ResponseEntity<UsersDto> response = restTemplate.getForEntity(UserController.REST_PATH, UsersDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UsersDto usersDto = response.getBody();
        assertNotNull(usersDto);
        assertNotNull(usersDto.getUsers());
        assertEquals(1, usersDto.getUsers().size());
    }

    @Test
    public void getUser() throws Exception {
        ResponseEntity<User> response = restTemplate.getForEntity(UserController.REST_PATH + UserController.BY_ID, User.class, "1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
