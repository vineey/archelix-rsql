package com.github.vineey.rql.querydsl.mongo.filter;

import com.github.vineey.rql.querydsl.filter.QueryDslFilterContext;
import com.querydsl.core.types.Path;

import java.util.Map;

/**
 * Created by mike on 10/1/16.
 */
public class MongoQuerydslFilterContext extends QueryDslFilterContext<MongoQuerydslFilterParam> {

    public static MongoQuerydslFilterContext withMapping(Map<String, Path> pathMapping) {
        return (MongoQuerydslFilterContext)new MongoQuerydslFilterContext()
                .setFilterVisitor(new MongoQuerydslFilterVisitor())
                .setFilterParam(new MongoQuerydslFilterParam().setMapping(pathMapping));
    }
}
