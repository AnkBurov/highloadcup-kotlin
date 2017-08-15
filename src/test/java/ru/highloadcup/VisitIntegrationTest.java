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
import ru.highloadcup.api.Visit;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.highloadcup.controller.VisitController.REST_PATH;
import static ru.highloadcup.util.HttpUtil.BY_ID;
import static ru.highloadcup.util.HttpUtil.NEW;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VisitIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createVisit() throws Exception {
        User user = UserIntegrationTest.createUserInternal(restTemplate, 1000000, "1236@gmail.com");
        Location location = LocationIntegrationTest.createLocationInternal(restTemplate, 1000000);
        createVisitInternal(restTemplate, 1000000, user.getId(), location.getId());
    }

    static Visit createVisitInternal(TestRestTemplate restTemplate, Integer id, Integer locationId, Integer userId) {
        Visit visit = new Visit();
        visit.setId(id);
        visit.setLocationId(locationId);
        visit.setUserId(userId);
        visit.setVisitedAt(0L);
        visit.setMark(5);

        ResponseEntity<EmptyJson> response = restTemplate.postForEntity(REST_PATH + NEW, visit, EmptyJson.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        return visit;
    }

    static void updateVisitInternal(TestRestTemplate restTemplate, Visit visit) {
        ResponseEntity<EmptyJson> response = restTemplate.postForEntity(REST_PATH + BY_ID, visit, EmptyJson.class, visit.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void updateVisit() throws Exception {
        User user = UserIntegrationTest.createUserInternal(restTemplate, 1000001, "127@gmail.com");
        Location location = LocationIntegrationTest.createLocationInternal(restTemplate, 1000001);
        Visit visit = createVisitInternal(restTemplate, 1000001, user.getId(), location.getId());
        visit.setMark(4);

        updateVisitInternal(restTemplate, visit);
    }

    @Test
    public void getVisit() throws Exception {
        User user = UserIntegrationTest.createUserInternal(restTemplate, 1000002, "1239@gmail.com");
        Location location = LocationIntegrationTest.createLocationInternal(restTemplate, 1000002);
        Visit visit = createVisitInternal(restTemplate, 1000002, user.getId(), location.getId());

        ResponseEntity<Visit> response = restTemplate.getForEntity(REST_PATH + BY_ID, Visit.class, visit.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(visit, response.getBody());
    }
}
