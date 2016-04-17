/*
* MIT License
*
* Copyright (c) 2016 John Michael Vincent S. Rustia
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package com.github.vineey.rql.querydsl.filter.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author vrustia on 10/10/2015.
 */
public final class DateUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;
    public final static DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public final static DateTimeFormatter LOCAL_TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);
    public final static DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    public static LocalTime parseLocalTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }

        return LocalTime.parse(time, LOCAL_TIME_FORMATTER);
    }

    public static String formatLocalTime(LocalTime time) {
        return time.format(LOCAL_TIME_FORMATTER);
    }


    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(LOCAL_DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(String dateTime) {
        if (StringUtils.isEmpty(dateTime)) {
            return null;
        }

        if (dateTime.length() == DATE_FORMAT.length()) {
            return LocalDateTime.of(LocalDate.parse(dateTime, LOCAL_DATE_FORMATTER), LocalTime.MIDNIGHT);
        }

        return LocalDateTime.parse(dateTime, LOCAL_DATE_TIME_FORMATTER);
    }


    public static String formatLocalDate(LocalDate localDate) {
        return localDate.format(LOCAL_DATE_FORMATTER);
    }

    public static LocalDate parseLocalDate(String dateTime) {
        if (StringUtils.isEmpty(dateTime)) {
            return null;
        }
        return LocalDate.parse(dateTime, LOCAL_DATE_FORMATTER);
    }

}
