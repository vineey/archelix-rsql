package com.archelix.rql.querydsl.filter.util;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * @author vrustia on 10/10/2015.
 */
public final class LocalTimeUtil {
    private LocalTimeUtil() {
    }

    public static final String TIME_FORMAT = "hh:mm:ss a";
    public final static DateTimeFormatter LOCAL_TIME_FORMATTER = new DateTimeFormatterBuilder().appendPattern(TIME_FORMAT).toFormatter();

    public static LocalTime parseLocalTime(String time) {
        return LocalTime.parse(time, LOCAL_TIME_FORMATTER);
    }

    public static String formatLocalTime(LocalTime time) {
        return time.toString(LOCAL_TIME_FORMATTER);
    }
}
