package com.github.vineey.rql.querydsl.jpa.filter;

import com.github.vineey.rql.querydsl.filter.QueryDslFilterContext;
import com.querydsl.core.types.Path;

import java.util.Map;

/**
 * Created by mike on 10/1/16.
 */
public class JpaQuerydslFilterContext extends QueryDslFilterContext<JpaQuerydslFilterParam> {

    public static JpaQuerydslFilterContext withMapping(Map<String, Path> pathMapping) {
        return (JpaQuerydslFilterContext)new JpaQuerydslFilterContext()
                .setFilterVisitor(new JpaQuerydslFilterVisitor())
                .setFilterParam(new JpaQuerydslFilterParam().setMapping(pathMapping));
    }

}
