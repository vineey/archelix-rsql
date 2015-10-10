package com.archelix.rql.querydsl.util;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.archelix.rql.querydsl.filter.QuerydslFilterBuilder;
import com.archelix.rql.querydsl.filter.QuerydslFilterParam;
import com.archelix.rql.querydsl.filter.util.RSQLUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.NumberPath;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static com.archelix.rql.filter.FilterManager.withBuilderAndParam;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    private FilterAssertUtil(){}

    private static final Logger LOG = LoggerFactory.getLogger(FilterAssertUtil.class);

    public static void  assertFilter(String selector, ComparisonOperator operator, String... arguments) {
        String[] rqlFilters = RSQLUtil.buildAllSymbols(selector, operator, arguments);

        for (String rqlFilter : rqlFilters) {
            LOG.debug("RQL Expression : {}", rqlFilter);
            FilterParser filterParser = new DefaultFilterParser();
            Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(NumberPath.class, selector)));
            assertNotNull(predicate);
            assertTrue(predicate instanceof BooleanOperation);
            BooleanOperation booleanOperation = (BooleanOperation) predicate;
            assertEquals(2, booleanOperation.getArgs().size());
            assertEquals(selector, booleanOperation.getArg(0).toString());
            assertEquals(operator.isMultiValue() ? PathTestUtil.pathArg(arguments) : arguments[0], booleanOperation.getArg(1).toString());
            assertEquals(operatorMapping.get(operator), booleanOperation.getOperator());
        }
    }

    //TODO to be abstracted for other converters/types
    private static QuerydslFilterParam createFilterParam(Class<? extends  Path> path, String... pathSelectors) {
        Class<? extends Number> numberClass = Long.class;
        QuerydslFilterParam querydslFilterParam = new QuerydslFilterParam();
        Map<String, Path> mapping = Maps.newHashMap();
        for (String pathSelector : pathSelectors)
            try {
                mapping.put(pathSelector, path.getConstructor(numberClass.getClass(), String.class).newInstance(numberClass, pathSelector));
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
}
