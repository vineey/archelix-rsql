package com.archelix.rql.querydsl.operator;

import com.google.common.collect.ImmutableMap;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.StringPath;

/**
 * @author vrustia on 9/26/2015.
 */
public final class PathOperatorContext {
    private PathOperatorContext(){}
    private final static ImmutableMap<Class<? extends Path>, PathOperator> map = ImmutableMap.<Class<? extends Path>, PathOperator>builder()
            .put(StringPath.class, new StringPathOperator())
            .build();

    public static PathOperator getOperator(Path path) {
        return map.get(path.getClass());
    }
}
