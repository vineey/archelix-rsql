package com.github.vineey.rql.querydsl.test.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {JpaBaseConfiguration.class})
public class MongoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MongoApplication.class);
    }

}