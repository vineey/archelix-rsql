package com.archelix.rql.querydsl.filter.converter;

import com.google.common.collect.ImmutableMap;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.EnumPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

/**
 * @author vrustia on 9/26/2015.
 */
public final class PathConverterContext {
    private PathConverterContext(){}
    private final static ImmutableMap<Class<? extends Path>, PathConverter> map = ImmutableMap.<Class<? extends Path>, PathConverter>builder()
            .put(StringPath.class, new StringPathConverter())
            .put(EnumPath.class, new EnumPathConverter())
            .put(NumberPath.class, new NumberPathConverter())
            .build();

    public static PathConverter getOperator(Path path) {
        return map.get(path.getClass());
    }
}
