package ru.highloadcup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.highloadcup.api.EmptyJson;
import ru.highloadcup.api.Location;
import ru.highloadcup.api.User;
import ru.highloadcup.api.UsersDto;
import ru.highloadcup.api.Visit;
import ru.highloadcup.api.VisitsDto;
import ru.highloadcup.controller.VisitController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.highloadcup.controller.UserController.REST_PATH;
import static ru.highloadcup.util.HttpUtil.BY_ID;
import static ru.highloadcup.util.HttpUtil.NEW;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getUsers() throws Exception {
        ResponseEntity<UsersDto> response = restTemplate.getForEntity(REST_PATH, UsersDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UsersDto usersDto = response.getBody();
        assertNotNull(usersDto);
        assertNotNull(usersDto.getUsers());
        assertEquals(1, usersDto.getUsers().size());
    }

    @Test
    public void createUser() throws Exception {
        createUserInternal(restTemplate, 700000, "123@gmail.com");
    }

    static User createUserInternal(TestRestTemplate restTemplate, Integer id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setFirstName("first");
        user.setLastName("second");
        user.setGender(User.Gender.m);
        user.setBirthDate(0L);

        ResponseEntity<EmptyJson> response = restTemplate.postForEntity(REST_PATH + NEW, user, EmptyJson.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        return user;
    }

    @Test
    public void updateUser() throws Exception {
        User user = createUserInternal(restTemplate, 700002, "1234@gmail.com");

        user.setEmail("321@gmail.com");
        updateUserInternal(restTemplate, user);
    }

    static void updateUserInternal(TestRestTemplate restTemplate, User user) {
        ResponseEntity<EmptyJson> response = restTemplate.postForEntity(REST_PATH + BY_ID, user, EmptyJson.class, user.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void getUser() throws Exception {
        User user = createUserInternal(restTemplate, 700003, "1235@gmail.com");

        user.setEmail("322@gmail.com");
        updateUserInternal(restTemplate, user);


        ResponseEntity<User> response = restTemplate.getForEntity(REST_PATH + BY_ID, User.class, user.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user, response.getBody());
    }

    @Test
    public void getVisits() throws Exception {
        User user = UserIntegrationTest.createUserInternal(restTemplate, 1000003, "1238@gmail.com");
        Location location = LocationIntegrationTest.createLocationInternal(restTemplate, 1000003);
        Visit visit = VisitIntegrationTest.createVisitInternal(restTemplate, 1000003, user.getId(), location.getId());

        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("id", user.getId());
        ResponseEntity<VisitsDto> response = restTemplate.getForEntity(
                REST_PATH + BY_ID + VisitController.REST_PATH + "/?country=" + location.getCountry(),
                VisitsDto.class, urlVariables);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
