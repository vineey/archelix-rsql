package com.github.vineey.rql.querydsl.mongo.sort;

import com.github.vineey.rql.querydsl.sort.QuerydslSortContext;
import com.querydsl.core.types.Path;

import java.util.Map;

/**
 * Created by mike on 10/1/16.
 */
public class MongoQuerydslSortContext extends QuerydslSortContext<MongoQuerydslSortParam> {
    public static MongoQuerydslSortContext withMapping(Map<String, Path> mappings) {
        return (MongoQuerydslSortContext) new MongoQuerydslSortContext()
                .setSortParam(new MongoQuerydslSortParam().setMapping(mappings))
                .setSortVisitor(new MongoQuerydslSortVisitor());
    }
}
