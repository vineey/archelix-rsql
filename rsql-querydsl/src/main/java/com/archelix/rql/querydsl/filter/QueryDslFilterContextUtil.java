package com.archelix.rql.querydsl.filter;

import com.archelix.rql.filter.FilterContext;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

import java.util.Map;

/**
 * @author vrustia - 3/26/16.
 */
public final class QueryDslFilterContextUtil {

    private QueryDslFilterContextUtil(){}

    public static FilterContext<Predicate, QuerydslFilterParam> withMapping(Map<String, Path> pathMapping) {
        return new FilterContext<Predicate, QuerydslFilterParam>()
                .setFilterBuilder(new QuerydslFilterBuilder())
                .setFilterParam(new QuerydslFilterParam().setMapping(pathMapping));
    }
}
