package com.github.vineey.rql.querydsl.filter.converter;

import com.github.vineey.rql.querydsl.filter.util.DateUtil;
import com.mysema.query.types.path.DateTimePath;
import java.time.LocalDateTime;

/**
 * @author vrustia on 10/12/2015.
 */
public class DateTimePathConverter extends AbstractTimeRangePathConverter<Comparable, DateTimePath> implements PathConverter<DateTimePath> {
    @Override
    protected Comparable convertArgument(Class<Comparable> pathFieldType, String argument) {
        if (ConverterConstant.NULL.equalsIgnoreCase(argument)) return null;
        else if (pathFieldType.equals(LocalDateTime.class)) return DateUtil.parseLocalDateTime(argument);

        throw new UnsupportedFieldClassException(pathFieldType, DateTimePath.class);
    }
}
