package ru.highloadcup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.highloadcup.api.AverageVisitMarkDto;
import ru.highloadcup.api.EmptyJson;
import ru.highloadcup.api.Location;
import ru.highloadcup.api.User;
import ru.highloadcup.api.Visit;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.highloadcup.controller.LocationController.AVG;
import static ru.highloadcup.controller.LocationController.REST_PATH;
import static ru.highloadcup.util.HttpUtil.BY_ID;
import static ru.highloadcup.util.HttpUtil.NEW;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createLocation() throws Exception {
        createLocationInternal(restTemplate, 600000);
    }

    static Location createLocationInternal(TestRestTemplate restTemplate, Integer id) {
        Location location = new Location();
        location.setId(id);
        location.setDistance(3);
        location.setCity("London");
        location.setCountry("England");
        location.setPlace("Some garden");

        ResponseEntity<EmptyJson> response = restTemplate.postForEntity(REST_PATH + NEW, location, EmptyJson.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        return location;
    }

    static void updateLocationInternal(TestRestTemplate restTemplate, Location location) {
        ResponseEntity<EmptyJson> response = restTemplate.postForEntity(REST_PATH + BY_ID, location, EmptyJson.class, location.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void updateLocation() throws Exception {
        Location location = createLocationInternal(restTemplate, 600001);
        location.setCity("Liverpool");
        updateLocationInternal(restTemplate, location);
    }

    @Test
    public void getLocation() throws Exception {
        Location location = createLocationInternal(restTemplate, 600002);
        location.setCity("Liverpool");
        updateLocationInternal(restTemplate, location);

        ResponseEntity<Location> response = restTemplate.getForEntity(REST_PATH + BY_ID, Location.class, location.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(location, response.getBody());
    }

    @Test
    public void getAverageVisitMark() throws Exception {
        User user = UserIntegrationTest.createUserInternal(restTemplate, 600003, "12399@gmail.com");
        Location location = LocationIntegrationTest.createLocationInternal(restTemplate, 600003);
        Visit visit = VisitIntegrationTest.createVisitInternal(restTemplate, 600003, user.getId(), location.getId());

        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("id", user.getId());

        ResponseEntity<AverageVisitMarkDto> response = restTemplate.getForEntity(
                REST_PATH + BY_ID + AVG + "?toAge=70", AverageVisitMarkDto.class, urlVariables);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
