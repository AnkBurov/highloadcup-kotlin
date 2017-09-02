package ru.highloadcup.warmer;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.highloadcup.api.Location;
import ru.highloadcup.api.User;
import ru.highloadcup.api.Visit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class Warmer {

    public void warmControllers() throws IOException, InterruptedException {
        TimeUnit.SECONDS.sleep(5L);
        System.out.println("Warm-up started");
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 1000; i++) {
            restTemplate.getForEntity("http://localhost/locations/1", Location.class);
            restTemplate.getForEntity("http://localhost/users/1", User.class);
            restTemplate.getForEntity("http://localhost/visits/1", Visit.class);
        }
        System.out.println("Warm-up ended");
    }
}
