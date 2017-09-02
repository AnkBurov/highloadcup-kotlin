package ru.highloadcup;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.highloadcup.parser.DataParser;
import ru.highloadcup.warmer.Warmer;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages = {"ru.highloadcup.controller", "ru.highloadcup.dao", "ru.highloadcup.warmer",
        "ru.highloadcup.parser", "ru.highloadcup.check"})
public class Application extends SpringBootServletInitializer implements InitializingBean {

    @Bean
    public DSLContext dsl(DataSource dataSource) {
        return new DefaultDSLContext(dataSource, SQLDialect.SQLITE);
    }

    @Autowired
    private DataParser dataParser;

    @Autowired
    private Warmer warmer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String dataFile = System.getProperty("dataFile");
        if (dataFile == null) {
            dataFile = "data.zip";
        }
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        final String finalDataFile = dataFile;
        taskExecutor.execute(() -> {
            try {
                dataParser.parse(Paths.get(finalDataFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        taskExecutor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5L);
                warmer.warmControllers();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
