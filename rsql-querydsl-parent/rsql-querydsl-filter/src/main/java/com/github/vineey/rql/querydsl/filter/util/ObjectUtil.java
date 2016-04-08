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

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.github.vineey.rql.querydsl.filter.converter.ConverterConstant.NULL;

/**
 * @author vrustia - 3/25/16.
 */
public final class ObjectUtil {

    public static Object convert(Class<?> clazz, String arg) {
        if (NULL.equalsIgnoreCase(arg)) {
            return null;
        } else if (clazz.isAssignableFrom(Number.class)) {
            Class<? extends Number> numberClass = (Class<? extends Number>) clazz;
            return convertToNumber(numberClass, arg);
        } else {
            throw new UnsupportedOperationException("Conversion not support for " + clazz);

        }

    }

    private static <E extends Number> Number convertToNumber(Class<E> clazz, String arg) {
        if (clazz.equals(Long.class)) {
            return NumberUtils.createLong(arg);
        } else if (clazz.equals(Double.class)) {
            return NumberUtils.createDouble(arg);
        } else if (clazz.equals(Integer.class)) {
            return NumberUtils.createInteger(arg);
        } else if (clazz.equals(BigDecimal.class)) {
            return NumberUtils.createBigDecimal(arg);
        } else if (clazz.equals(Short.class)) {
            return new Short(arg);
        } else if (clazz.equals(BigInteger.class)) {
            return NumberUtils.createBigInteger(arg);
        } else if (clazz.equals(Float.class)) {
            return NumberUtils.createFloat(arg);
        } else {
            throw new UnsupportedOperationException("Conversion to Number doesn't support " + clazz);
        }
    }
}
