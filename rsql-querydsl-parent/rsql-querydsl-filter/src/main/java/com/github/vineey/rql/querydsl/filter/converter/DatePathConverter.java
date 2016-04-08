package com.github.vineey.rql.querydsl.filter.converter;

import com.github.vineey.rql.querydsl.filter.util.DateUtil;
import com.mysema.query.types.path.DatePath;
import java.time.LocalDate;

/**
 * @author vrustia on 10/12/2015.
 */
public class DatePathConverter extends AbstractTimeRangePathConverter<Comparable, DatePath> implements PathConverter<DatePath> {
    @Override
    protected Comparable convertArgument(Class<Comparable> pathFieldType, String argument) {
        if (ConverterConstant.NULL.equalsIgnoreCase(argument)) return null;
        else if (pathFieldType.equals(LocalDate.class)) return DateUtil.parseLocalDate(argument);

        throw new UnsupportedFieldClassException(pathFieldType, DatePath.class);
    }
}
