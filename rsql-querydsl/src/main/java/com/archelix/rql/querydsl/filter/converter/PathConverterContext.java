package com.archelix.rql.querydsl.filter.converter;

import com.google.common.collect.ImmutableMap;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.*;

import java.util.Collection;

/**
 * @author vrustia on 9/26/2015.
 */
public final class PathConverterContext {
    private static final StringPathConverter STRING_PATH_CONVERTER = new StringPathConverter();
    private static final EnumPathConverter ENUM_PATH_CONVERTER = new EnumPathConverter();
    private static final NumberPathConverter NUMBER_PATH_CONVERTER = new NumberPathConverter();
    private static final BooleanPathConverter BOOLEAN_PATH_CONVERTER = new BooleanPathConverter();
    private static final TimePathConverter TIME_PATH_CONVERTER = new TimePathConverter();
    private static final DateTimePathConverter DATE_TIME_PATH_CONVERTER = new DateTimePathConverter();
    private static final DatePathConverter DATE_PATH_CONVERTER = new DatePathConverter();
    private static final DefaultCollectionPathConverter<Object, SimpleExpression<? super Object>, Collection<Object>, CollectionPathBase<Collection<Object>, Object, SimpleExpression<? super Object>>> DEFAULT_COLLECTION_PATH_CONVERTER = new DefaultCollectionPathConverter<>();
    private final static ImmutableMap<Class<? extends Path>, PathConverter> map = ImmutableMap.<Class<? extends Path>, PathConverter>builder()
            .put(StringPath.class, STRING_PATH_CONVERTER)
            .put(EnumPath.class, ENUM_PATH_CONVERTER)
            .put(NumberPath.class, NUMBER_PATH_CONVERTER)
            .put(BooleanPath.class, BOOLEAN_PATH_CONVERTER)
            .put(TimePath.class, TIME_PATH_CONVERTER)
            .put(DateTimePath.class, DATE_TIME_PATH_CONVERTER)
            .put(DatePath.class, DATE_PATH_CONVERTER)
            .put(ListPath.class, DEFAULT_COLLECTION_PATH_CONVERTER)
            .put(SetPath.class, DEFAULT_COLLECTION_PATH_CONVERTER)
            .put(CollectionPath.class, DEFAULT_COLLECTION_PATH_CONVERTER)
            .build();

    private PathConverterContext() {
    }

    public static PathConverter getOperator(Path path) {
        return map.get(path.getClass());
    }
}
