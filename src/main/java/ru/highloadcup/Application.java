package ru.highloadcup;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import ru.highloadcup.parser.DataParser;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Paths;

@SpringBootApplication(scanBasePackages = {"ru.highloadcup.controller", "ru.highloadcup.dao"})
public class Application implements InitializingBean {

    @Bean
    public DataParser dataParser() {
        return new DataParser();
    }

    @Bean
    public DSLContext dsl(DataSource dataSource) {
        return new DefaultDSLContext(dataSource, SQLDialect.SQLITE);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
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
                dataParser().parse(Paths.get(finalDataFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
