package com.archelix.rql.querydsl.filter;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.archelix.rql.querydsl.filter.util.DateUtil;
import com.archelix.rql.querydsl.filter.util.RSQLUtil;
import com.archelix.rql.querydsl.util.FilterAssertUtil;
import com.google.common.collect.Maps;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.TimePath;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import java.time.LocalTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeParseException;
import java.util.Map;

import static com.archelix.rql.filter.FilterManager.withBuilderAndParam;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * @author vrustia on 9/27/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_TimePath_Test {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final Logger LOG = LoggerFactory.getLogger(QuerydslFilterBuilder_TimePath_Test.class);

    @Test
    public void testParse_TimeEquals_NullValue() {
        String selector = "startTime";
        String argument = "null";
        for (ComparisonOperator comparisonOperator : asList(EQUAL, NOT_EQUAL)) {
            String rqlFilter = RSQLUtil.build(selector, comparisonOperator, argument);

            LOG.debug("RQL Expression : {}", rqlFilter);
            FilterParser filterParser = new DefaultFilterParser();
            Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
            assertNotNull(predicate);
            assertTrue(predicate instanceof BooleanOperation);
            BooleanOperation booleanOperation = (BooleanOperation) predicate;
            assertEquals(1, booleanOperation.getArgs().size());
            assertEquals(selector, booleanOperation.getArg(0).toString());
            assertEquals(EQUAL.equals(comparisonOperator) ? Ops.IS_NULL : Ops.IS_NOT_NULL, booleanOperation.getOperator());
        }

    }

    @Test
    public void testParse_TimeOtherOperator_NullValue() {
        String selector = "startTime";
        String argument = null;
        for (ComparisonOperator comparisonOperator : asList(GREATER_THAN, GREATER_THAN_OR_EQUAL,
                LESS_THAN, LESS_THAN_OR_EQUAL, IN, NOT_IN)) {
            try {
                RSQLUtil.build(selector, comparisonOperator, argument);
            } catch (Exception e) {
                assertEquals(IllegalArgumentException.class, e.getClass());
            }
        }

    }

    @Test
    public void testParse_TimeEquals_EmptyValue() {
        String selector = "startTime";
        for (ComparisonOperator comparisonOperator : asList(GREATER_THAN, GREATER_THAN_OR_EQUAL,
                LESS_THAN, LESS_THAN_OR_EQUAL, IN, NOT_IN)) {
            String rqlFilter = "startTime" + comparisonOperator.getSymbol();

            LOG.debug("RQL Expression : {}", rqlFilter);
            FilterParser filterParser = new DefaultFilterParser();
            try {
                filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
            } catch (Exception e) {
                assertEquals(RSQLParserException.class, e.getClass());
            }
        }
    }

    @Test
    public void testParse_TimeEquals_AM() {
        String selector = "startTime";
        String argument = "10:00:00";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, formatLocalTime(booleanOperation));
        assertEquals(Ops.EQ, booleanOperation.getOperator());

    }

    @Test
    public void testParse_TimeEquals_PM() {
        String selector = "startTime";
        String argument = "22:00:00";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, formatLocalTime(booleanOperation));
        assertEquals(Ops.EQ, booleanOperation.getOperator());

    }

    private String formatLocalTime(BooleanOperation booleanOperation) {
        return DateUtil.formatLocalTime(getLocalTimeArg(booleanOperation));
    }

    private LocalTime getLocalTimeArg(BooleanOperation booleanOperation) {
        return (LocalTime) ((ConstantImpl) booleanOperation.getArg(1)).getConstant();
    }

    @Test
    public void testParse_TimeNotEquals() {
        String selector = "startTime";
        String argument = "10:00:00";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.NOT_EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, formatLocalTime(booleanOperation));
        assertEquals(Ops.NE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_TimeGreaterThan() {
        String selector = "startTime";
        String argument = "10:00:00";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.GREATER_THAN, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, formatLocalTime(booleanOperation));
        assertEquals(Ops.GT, booleanOperation.getOperator());
    }

    @Test
    public void testParse_TimeGreaterThanOrEquals() {
        String selector = "startTime";
        String argument = "10:00:00";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.GREATER_THAN_OR_EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, formatLocalTime(booleanOperation));
        assertEquals(Ops.GOE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_TimeLessThan() {
        String selector = "startTime";
        String argument = "10:00:00";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.LESS_THAN, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, formatLocalTime(booleanOperation));
        assertEquals(Ops.LT, booleanOperation.getOperator());
    }

    @Test
    public void testParse_TimeLessThanOrEquals() {
        String selector = "startTime";
        String argument = "10:00:00";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.LESS_THAN_OR_EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, formatLocalTime(booleanOperation));
        assertEquals(Ops.LOE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_TimeIn() {
        String selector = "startTime";
        String argument = "10:00:00";
        String argument2 = "11:00:00";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.IN, argument, argument2);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals("[10:00, 11:00]", booleanOperation.getArg(1).toString());
        assertEquals(Ops.IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_TimeNotIn() {
        String selector = "startTime";
        String argument = "10:00:00";
        String argument2 = "11:00:00";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.NOT_IN, argument, argument2);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals("[10:00, 11:00]", booleanOperation.getArg(1).toString());
        assertEquals(Ops.NOT_IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_Time_NotATimeArgument() {
        String selector = "age";
        String argument = "FE";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        thrown.expect(DateTimeParseException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(LocalTime.class, selector)));
    }

    private QuerydslFilterParam createFilterParam(Class<? extends Comparable> numberClass, String... pathSelectors) {
        QuerydslFilterParam querydslFilterParam = new QuerydslFilterParam();
        Map<String, Path> mapping = Maps.newHashMap();
        for (String pathSelector : pathSelectors)
            mapping.put(pathSelector, new TimePath(numberClass, pathSelector));
        querydslFilterParam.setMapping(mapping);
        return querydslFilterParam;

    }
}
