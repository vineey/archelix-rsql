package com.archelix.rql.querydsl.filter.converter;

import com.archelix.rql.querydsl.filter.util.DateUtil;
import com.mysema.query.types.path.DateTimePath;
import java.time.LocalDateTime;

import static com.archelix.rql.querydsl.filter.converter.ConverterConstant.NULL;

/**
 * @author vrustia on 10/12/2015.
 */
public class DateTimePathConverter extends AbstractTimeRangePathConverter<Comparable, DateTimePath> implements PathConverter<DateTimePath> {
    @Override
    protected Comparable convertArgument(Class<Comparable> pathFieldType, String argument) {
        if (NULL.equalsIgnoreCase(argument)) return null;
        else if (pathFieldType.equals(LocalDateTime.class)) return DateUtil.parseLocalDateTime(argument);

        throw new UnsupportedFieldClassException(pathFieldType, DateTimePath.class);
    }
}
