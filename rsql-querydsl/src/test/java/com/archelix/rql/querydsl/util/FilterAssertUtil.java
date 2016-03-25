package com.archelix.rql.querydsl.util;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.archelix.rql.querydsl.filter.QuerydslFilterBuilder;
import com.archelix.rql.querydsl.filter.QuerydslFilterParam;
import com.archelix.rql.querydsl.filter.util.RSQLUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.*;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
            .put(String.class, withConstructor(StringPath.class))
            .put(Long.class, withConstructor(NumberPath.class, Long.class))
            .put(BigDecimal.class, withConstructor(NumberPath.class, BigDecimal.class))
            .put(Integer.class, withConstructor(NumberPath.class, Integer.class))
            .put(Boolean.class, withConstructor(BooleanPath.class, null))
            .put(LocalTime.class, withConstructor(TimePath.class, LocalTime.class))
            .put(LocalDateTime.class, withConstructor(DateTimePath.class, LocalDateTime.class))
            .put(LocalDate.class, withConstructor(DatePath.class, LocalDate.class))
            .build();
    private static final Logger LOG = LoggerFactory.getLogger(FilterAssertUtil.class);

    private FilterAssertUtil() {
    }

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
            assertEquals(getOpMapping(operator), booleanOperation.getOperator());
        }
    }

    public static Operator getOpMapping(ComparisonOperator operator) {
        return operatorMapping.get(operator);
    }

    public static <T extends Map.Entry<String, Class>> QuerydslFilterParam withFilterParam(T... pathMappings) {
        return withFilterParam(ImmutableList.copyOf(pathMappings));
    }

    public static QuerydslFilterParam withFilterParam(List<Map.Entry<String, Class>> pathMappings) {
        QuerydslFilterParam querydslFilterParam = new QuerydslFilterParam();
        querydslFilterParam.setMapping(buildMapping(pathMappings));
        return querydslFilterParam;
    }

    //still supports only
    public static QuerydslFilterParam withFilterParam(Class<?> fieldType, String... pathSelectors) {

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
                pathMappings.put(pathSelector, pathClass.getConstructor(constructorDef).newInstance(constructorParam));
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
