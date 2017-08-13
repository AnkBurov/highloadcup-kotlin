package ru.highloadcup.warmer;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.highloadcup.api.Location;
import ru.highloadcup.api.User;
import ru.highloadcup.api.Visit;

import java.io.IOException;

@Component
public class Warmer {

    public void warmControllers() throws IOException, InterruptedException {
        System.out.println("Warm-up started");
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 10; i++) {
            restTemplate.getForEntity("http://localhost/locations/1", Location.class);
            restTemplate.getForEntity("http://localhost/users/1", User.class);
            restTemplate.getForEntity("http://localhost/visits/1", Visit.class);
        }
        System.out.println("Warm-up ended");
    }
}
