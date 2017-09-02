package ru.highloadcup.dao;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/hello")
public class DefaultController {

    @GetMapping
    public void getDefault(HttpServletRequest request, HttpServletResponse response) {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.start(() -> {
            try {
                try (PrintWriter writer = response.getWriter()) {
                    writer.write("it works");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            asyncContext.complete();
        });

//        return "It works";
    }

    /*private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

//    static {
//        executorService.scheduleAtFixedRate(AsyncContext::newEvent, 0, 2, TimeUnit.SECONDS);
    }

    private static void newEvent() {
        ArrayList clients = new ArrayList<>(queue.size());
        queue.drainTo(clients);
        clients.parallelStream().forEach((AsyncContext ac) -> {
            ServletUtil.writeResponse(ac.getResponse(), "OK");
            ac.complete();
        });
    }

    private static final BlockingQueue queue = new ArrayBlockingQueue<>(20000);

    public static void addToWaitingList(AsyncContext c) {
        queue.ad*/
//    }
}
