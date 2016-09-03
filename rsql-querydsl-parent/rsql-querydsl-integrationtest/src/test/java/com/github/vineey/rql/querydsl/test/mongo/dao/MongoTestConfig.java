package com.github.vineey.rql.querydsl.test.mongo.dao;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by vine on 9/3/16.
 */

@Configuration
@EnableMongoRepositories(basePackageClasses = {ContactMongoDao.class})
public class MongoTestConfig  extends AbstractMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "test";
    }
    @Bean
    public Mongo mongo()  throws Exception {
        return new Fongo("test").getMongo();
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongo(), "test");
        return mongoTemplate;
    }

    protected String getMappingBasePackage() {
        return "com.github.vineey.rql.querydsl.test.mongo";
    }
}
