package com.github.vineey.rql.querydsl.filter.converter;

import com.github.vineey.rql.querydsl.filter.util.DateUtil;
import com.mysema.query.types.path.TimePath;
import java.time.LocalTime;

/**
 * @author vrustia on 10/10/2015.
 */
public class TimePathConverter extends AbstractTimeRangePathConverter<Comparable, TimePath> implements PathConverter<TimePath> {

    protected Comparable convertArgument(Class<Comparable> pathFieldType, String argument) {
        //TODO convert arg to time with default format
        if (ConverterConstant.NULL.equalsIgnoreCase(argument)) return null;
        else if (pathFieldType.equals(LocalTime.class)) return DateUtil.parseLocalTime(argument);

        throw new UnsupportedFieldClassException(pathFieldType, TimePath.class);
    }
}
