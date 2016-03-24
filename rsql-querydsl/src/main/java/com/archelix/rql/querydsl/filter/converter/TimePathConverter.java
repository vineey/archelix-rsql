package com.archelix.rql.querydsl.filter.converter;

import com.archelix.rql.querydsl.filter.util.DateUtil;
import com.mysema.query.types.path.TimePath;
import java.time.LocalTime;

import static com.archelix.rql.querydsl.filter.converter.ConverterConstant.NULL;

/**
 * @author vrustia on 10/10/2015.
 */
public class TimePathConverter extends AbstractTimeRangePathConverter<Comparable, TimePath> implements PathConverter<TimePath> {

    protected Comparable convertArgument(Class<Comparable> pathFieldType, String argument) {
        //TODO convert arg to time with default format
        if (NULL.equalsIgnoreCase(argument)) return null;
        else if (pathFieldType.equals(LocalTime.class)) return DateUtil.parseLocalTime(argument);

        throw new UnsupportedFieldClassException(pathFieldType, TimePath.class);
    }
}
