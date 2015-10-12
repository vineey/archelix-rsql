package com.archelix.rql.querydsl.filter.util;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author vrustia on 10/10/2015.
 */
public final class DateUtil {
    private DateUtil() {
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;
    public final static DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT);
    public final static DateTimeFormatter LOCAL_TIME_FORMATTER = DateTimeFormat.forPattern(TIME_FORMAT);
    public final static DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATETIME_FORMAT);

    public static LocalTime parseLocalTime(String time) {
        return LocalTime.parse(time, LOCAL_TIME_FORMATTER);
    }

    public static String formatLocalTime(LocalTime time) {
        return time.toString(LOCAL_TIME_FORMATTER);
    }


    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.toString(LOCAL_DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, LOCAL_DATE_TIME_FORMATTER);
    }


    public static String formatLocalDate(LocalDate localDate) {
        return localDate.toString(LOCAL_DATE_FORMATTER);
    }

    public static LocalDate parseLocalDate(String dateTime) {
        return LocalDate.parse(dateTime, LOCAL_DATE_FORMATTER);
    }

}
