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
package com.github.vineey.rql.querydsl.jpa.util;

import com.github.vineey.rql.filter.parser.DefaultFilterParser;
import com.github.vineey.rql.filter.parser.FilterParser;
import com.github.vineey.rql.querydsl.filter.util.RSQLUtil;
import com.github.vineey.rql.querydsl.jpa.filter.JpaQuerydslFilterParam;
import com.github.vineey.rql.querydsl.jpa.filter.JpaQuerydslFilterVisitor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.github.vineey.rql.querydsl.jpa.filter.JpaQuerydslFilterContext.withBuilderAndParam;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author vrustia on 9/27/2015.
 */
public final class FilterAssertUtil {

    private static final ImmutableMap<ComparisonOperator, Ops> operatorMapping = ImmutableMap
            .<ComparisonOperator, Ops>builder()
            .put(RSQLOperators.EQUAL, Ops.EQ)
            .put(RSQLOperators.NOT_EQUAL, Ops.NE)
            .put(RSQLOperators.IN, Ops.IN)
            .put(RSQLOperators.NOT_IN, Ops.NOT_IN)
            .put(RSQLOperators.LESS_THAN, Ops.LT)
            .put(RSQLOperators.LESS_THAN_OR_EQUAL, Ops.LOE)
            .put(RSQLOperators.GREATER_THAN, Ops.GT)
            .put(RSQLOperators.GREATER_THAN_OR_EQUAL, Ops.GOE)
            .build();

    private static final ImmutableMap<Class, PathConstructorInfo> pathTypeMapping = ImmutableMap
            .<Class, PathConstructorInfo>builder()
            .put(String.class, PathConstructorInfo.withConstructor(StringPath.class))
            .put(Long.class, PathConstructorInfo.withConstructor(NumberPath.class, Long.class))
            .put(BigDecimal.class, PathConstructorInfo.withConstructor(NumberPath.class, BigDecimal.class))
            .put(Integer.class, PathConstructorInfo.withConstructor(NumberPath.class, Integer.class))
            .put(Boolean.class, PathConstructorInfo.withConstructor(BooleanPath.class, null))
            .put(LocalTime.class, PathConstructorInfo.withConstructor(TimePath.class, LocalTime.class))
            .put(LocalDateTime.class, PathConstructorInfo.withConstructor(DateTimePath.class, LocalDateTime.class))
            .put(LocalDate.class, PathConstructorInfo.withConstructor(DatePath.class, LocalDate.class))
            .build();
    private static final Logger LOG = LoggerFactory.getLogger(FilterAssertUtil.class);

    private FilterAssertUtil() {
    }

    public static void assertFilter(Class<?> fieldType, String selector, ComparisonOperator operator, String... arguments) {
        String[] rqlFilters = RSQLUtil.buildAllSymbols(selector, operator, arguments);

        for (String rqlFilter : rqlFilters) {
            LOG.debug("RQL Expression : {}", rqlFilter);
            FilterParser filterParser = new DefaultFilterParser();
            Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new JpaQuerydslFilterVisitor(), withFilterParam(fieldType, selector)));
            assertNotNull(predicate);
            assertTrue(predicate instanceof BooleanOperation);
            BooleanOperation booleanOperation = (BooleanOperation) predicate;
            Assert.assertEquals(2, booleanOperation.getArgs().size());
            Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
            Assert.assertEquals(operator.isMultiValue() ? PathTestUtil.pathArg(arguments) : arguments[0], booleanOperation.getArg(1).toString());
            Assert.assertEquals(getOpMapping(operator), booleanOperation.getOperator());
        }
    }

    public static Ops getOpMapping(ComparisonOperator operator) {
        return operatorMapping.get(operator);
    }

    public static <T extends Map.Entry<String, Class>> JpaQuerydslFilterParam withFilterParam(T... pathMappings) {
        return withFilterParam(ImmutableList.copyOf(pathMappings));
    }

    public static JpaQuerydslFilterParam withFilterParam(List<Map.Entry<String, Class>> pathMappings) {
        JpaQuerydslFilterParam querydslFilterParam = new JpaQuerydslFilterParam();
        querydslFilterParam.setMapping(buildMapping(pathMappings));
        return querydslFilterParam;
    }

    public static Map<String, Path> buildPathMap(Class<?> fieldType, String... pathSelectors) {

        List<Map.Entry<String, Class>> selectorClassMappings = new ArrayList<>();
        for (String selector : pathSelectors) {
            selectorClassMappings.add(new LinkedHashMap.SimpleEntry<>(selector, fieldType));
        }

        return buildMapping(selectorClassMappings);

    }

    //still supports only
    public static JpaQuerydslFilterParam withFilterParam(Class<?> fieldType, String... pathSelectors) {

        List<Map.Entry<String, Class>> selectorClassMappings = new ArrayList<>();
        for (String selector : pathSelectors) {
            selectorClassMappings.add(new LinkedHashMap.SimpleEntry<>(selector, fieldType));
        }

        return withFilterParam(selectorClassMappings);

    }

    private static Object[] withConstructorParam(Class subClass, String pathSelector) {
        Object[] constructorParam;
        if (subClass != null) {
            constructorParam = new Object[]{subClass, pathSelector};
        } else {
            constructorParam = new Object[]{pathSelector};
        }
        return constructorParam;
    }

    private static Class[] withConstructorDef(Class subClass) {
        Class[] constructorDef;
        if (subClass != null) {
            constructorDef = new Class[]{subClass.getClass(), String.class};
        } else {
            constructorDef = new Class[]{String.class};
        }
        return constructorDef;
    }

    private static Map<String, Path> buildMapping(List<Map.Entry<String, Class>> selectorClassMappings) {
        Map<String, Path> pathMappings = new HashMap<>();
        for (Map.Entry<String, Class> selectorMapping : selectorClassMappings) {
            String pathSelector = selectorMapping.getKey();
            Class<?> fieldType = selectorMapping.getValue();
            PathConstructorInfo pathConstructorInfo = pathTypeMapping.get(fieldType);
            Class<? extends Path> pathClass = pathConstructorInfo.getPathClass();
            Class subClass = pathConstructorInfo.getFieldType();
            Class[] constructorDef = withConstructorDef(subClass);
            try {
                Object[] constructorParam = withConstructorParam(subClass, pathSelector);
                Constructor<? extends Path> constructor = pathClass.getDeclaredConstructor(constructorDef);
                constructor.setAccessible(true);
                pathMappings.put(pathSelector, constructor.newInstance(constructorParam));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return pathMappings;
    }
}
