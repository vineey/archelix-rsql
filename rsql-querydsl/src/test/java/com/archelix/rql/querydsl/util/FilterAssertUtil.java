package com.archelix.rql.querydsl.util;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.archelix.rql.querydsl.filter.QuerydslFilterBuilder;
import com.archelix.rql.querydsl.filter.QuerydslFilterParam;
import com.archelix.rql.querydsl.filter.util.RSQLUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mysema.query.types.*;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.*;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Map;

import static com.archelix.rql.filter.FilterManager.withBuilderAndParam;
import static com.archelix.rql.querydsl.util.PathConstructorInfo.withConstructor;
import static org.junit.Assert.*;

/**
 * @author vrustia on 9/27/2015.
 */
public final class FilterAssertUtil {

    private static final ImmutableMap<ComparisonOperator, Operator> operatorMapping = ImmutableMap
            .<ComparisonOperator, Operator>builder()
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
            .put(Long.class, withConstructor(NumberPath.class, Long.class))
            .put(BigDecimal.class, withConstructor(NumberPath.class, BigDecimal.class))
            .put(Boolean.class, withConstructor(BooleanPath.class, null))
            .put(LocalTime.class, withConstructor(TimePath.class, LocalTime.class))
            .put(LocalDateTime.class, withConstructor(DateTimePath.class, LocalDateTime.class))
            .put(LocalDate.class, withConstructor(DatePath.class, LocalDate.class))
            .build();

    private FilterAssertUtil() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(FilterAssertUtil.class);

    public static void assertFilter(Class<?> fieldType, String selector, ComparisonOperator operator, String... arguments) {
        String[] rqlFilters = RSQLUtil.buildAllSymbols(selector, operator, arguments);

        for (String rqlFilter : rqlFilters) {
            LOG.debug("RQL Expression : {}", rqlFilter);
            FilterParser filterParser = new DefaultFilterParser();
            Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), withFilterParam(fieldType, selector)));
            assertNotNull(predicate);
            assertTrue(predicate instanceof BooleanOperation);
            BooleanOperation booleanOperation = (BooleanOperation) predicate;
            assertEquals(2, booleanOperation.getArgs().size());
            assertEquals(selector, booleanOperation.getArg(0).toString());
            assertEquals(operator.isMultiValue() ? PathTestUtil.pathArg(arguments) : arguments[0], booleanOperation.getArg(1).toString());
            assertEquals(operatorMapping.get(operator), booleanOperation.getOperator());
        }
    }

    //still supports only
    public static QuerydslFilterParam withFilterParam(Class<?> fieldType, String... pathSelectors) {
        PathConstructorInfo pathConstructorInfo = pathTypeMapping.get(fieldType);
        Class<? extends Path> pathClass = pathConstructorInfo.getPathClass();
        Class subClass = pathConstructorInfo.getFieldType();
        Class[] constructorDef = withConstructorDef(subClass);
        QuerydslFilterParam querydslFilterParam = new QuerydslFilterParam();
        Map<String, Path> mapping = Maps.newHashMap();
        for (String pathSelector : pathSelectors)
            try {
                Object[] constructorParam = withConstructorParam(subClass, pathSelector);
                mapping.put(pathSelector, pathClass.getConstructor(constructorDef).newInstance(constructorParam));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        querydslFilterParam.setMapping(mapping);
        return querydslFilterParam;

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
}
