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
