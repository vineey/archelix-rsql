package com.archelix.rql.querydsl;

import com.archelix.rql.filter.FilterParam;
import com.mysema.query.types.Path;

import java.util.Map;

/**
 * @author vrustia on 9/26/2015.
 */
public class QuerydslFilterParam extends FilterParam {
    private Map<String, Path> mapping;

    public Map<String, Path> getMapping() {
        return mapping;
    }

    public QuerydslFilterParam setMapping(Map<String, Path> mapping) {
        this.mapping = mapping;
        return this;
    }
}
