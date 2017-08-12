package ru.highloadcup;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.client.RestTemplate;
import ru.highloadcup.api.User;

public class LoadTest {

//    @Test
    public void test() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();

        for (int i = 0; i < 100; i++) {
            taskExecutor.execute(() -> restTemplate.getForEntity("http://localhost:8080/users/1", User.class, "1"));
        }
    }
}
