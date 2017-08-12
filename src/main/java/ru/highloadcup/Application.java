package ru.highloadcup;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = {"ru.highloadcup.controller", "ru.highloadcup.dao"})
public class Application {

    @Bean
    public DSLContext dsl(DataSource dataSource) {
        return new DefaultDSLContext(dataSource, SQLDialect.SQLITE);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
