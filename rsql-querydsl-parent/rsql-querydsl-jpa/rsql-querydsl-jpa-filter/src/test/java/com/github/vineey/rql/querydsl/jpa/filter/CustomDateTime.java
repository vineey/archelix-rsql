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
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* 
*/
package com.github.vineey.rql.querydsl.jpa.filter;

import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;

/**
 * @author vrustia - 4/17/16.
 */
public class CustomDateTime implements Temporal, TemporalAdjuster, ChronoLocalDateTime<LocalDate> {
    @Override
    public LocalDate toLocalDate() {
        return null;
    }

    @Override
    public LocalTime toLocalTime() {
        return null;
    }

    @Override
    public ChronoLocalDateTime<LocalDate> with(TemporalField field, long newValue) {
        return null;
    }

    @Override
    public ChronoLocalDateTime<LocalDate> plus(long amountToAdd, TemporalUnit unit) {
        return null;
    }

    @Override
    public ChronoZonedDateTime<LocalDate> atZone(ZoneId zone) {
        return null;
    }

    @Override
    public long until(Temporal endExclusive, TemporalUnit unit) {
        return 0;
    }

    @Override
    public boolean isSupported(TemporalField field) {
        return false;
    }

    @Override
    public long getLong(TemporalField field) {
        return 0;
    }

    @Override
    public Temporal adjustInto(Temporal temporal) {
        return null;
    }

    @Override
    public int compareTo(ChronoLocalDateTime<?> other) {
        return 0;
    }

    @Override
    public String format(DateTimeFormatter formatter) {
        return null;
    }

    @Override
    public Chronology getChronology() {
        return null;
    }

    @Override
    public boolean isAfter(ChronoLocalDateTime<?> other) {
        return false;
    }

    @Override
    public boolean isBefore(ChronoLocalDateTime<?> other) {
        return false;
    }

    @Override
    public boolean isEqual(ChronoLocalDateTime<?> other) {
        return false;
    }

    @Override
    public boolean isSupported(TemporalUnit unit) {
        return false;
    }

    @Override
    public ChronoLocalDateTime<LocalDate> minus(TemporalAmount amount) {
        return null;
    }

    @Override
    public ChronoLocalDateTime<LocalDate> minus(long amountToSubtract, TemporalUnit unit) {
        return null;
    }

    @Override
    public ChronoLocalDateTime<LocalDate> plus(TemporalAmount amount) {
        return null;
    }

    @Override
    public <R> R query(TemporalQuery<R> query) {
        return null;
    }

    @Override
    public long toEpochSecond(ZoneOffset offset) {
        return 0;
    }

    @Override
    public Instant toInstant(ZoneOffset offset) {
        return null;
    }

    @Override
    public ChronoLocalDateTime<LocalDate> with(TemporalAdjuster adjuster) {
        return null;
    }

    @Override
    public int get(TemporalField field) {
        return 0;
    }

    @Override
    public ValueRange range(TemporalField field) {
        return null;
    }
}
