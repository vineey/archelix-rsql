package com.archelix.rql.querydsl.filter;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.archelix.rql.querydsl.filter.util.DateUtil;
import com.archelix.rql.querydsl.filter.util.RSQLUtil;
import com.archelix.rql.querydsl.util.FilterAssertUtil;
import com.archelix.rql.querydsl.util.PathTestUtil;
import com.google.common.collect.Maps;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.DateTimePath;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeParseException;
import java.util.Map;

import static com.archelix.rql.filter.FilterContext.withBuilderAndParam;
import static org.junit.Assert.*;

/**
 * @author vrustia on 9/27/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_DateTimePath_Test {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final Logger LOG = LoggerFactory.getLogger(QuerydslFilterBuilder_DateTimePath_Test.class);

    private String formatLocalDateTime(BooleanOperation booleanOperation) {
        return DateUtil.formatLocalDateTime(getLocalDateTimeArg(booleanOperation));
    }

    private LocalDateTime getLocalDateTimeArg(BooleanOperation booleanOperation) {
        return (LocalDateTime) ((ConstantImpl) booleanOperation.getArg(1)).getConstant();
    }

    @Test
    public void testParse_DateTimeEquals_AM() {
        String selector = "startTime";
        String argument = "'2014-09-09 10:00:00'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDateTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        assertEquals(Ops.EQ, booleanOperation.getOperator());

    }

    @Test
    public void testParse_DateTimeEquals_PM() {
        String selector = "startTime";
        String argument = "'2014-09-09 21:00:00'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDateTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        assertEquals(Ops.EQ, booleanOperation.getOperator());

    }

    @Test
    public void testParse_DateTimeNotEquals() {
        String selector = "startTime";
        String argument = "'2014-09-09 10:00:00'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.NOT_EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDateTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        assertEquals(Ops.NE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateTimeGreaterThan() {
        String selector = "startTime";
        String argument = "'2014-09-09 10:00:00'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.GREATER_THAN, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDateTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        assertEquals(Ops.GT, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateTimeGreaterThanOrEquals() {
        String selector = "startTime";
        String argument = "'2014-09-09 10:00:00'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.GREATER_THAN_OR_EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDateTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        assertEquals(Ops.GOE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateTimeLessThan() {
        String selector = "startTime";
        String argument = "'2014-09-09 10:00:00'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.LESS_THAN, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDateTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        assertEquals(Ops.LT, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateTimeLessThanOrEquals() {
        String selector = "startTime";
        String argument = "'2014-09-09 10:00:00'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.LESS_THAN_OR_EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDateTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        assertEquals(Ops.LOE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateTimeIn() {
        String selector = "startTime";
        String argument = "'2014-09-09 10:00:00'";
        String argument2 = "'2014-09-09 11:00:00'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.IN, argument, argument2);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDateTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals("[2014-09-09T10:00, 2014-09-09T11:00]", booleanOperation.getArg(1).toString());
        assertEquals(Ops.IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateTimeNotIn() {
        String selector = "startTime";
        String argument = "'2014-09-09 10:00:00'";
        String argument2 = "'2014-09-09 11:00:00'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.NOT_IN, argument, argument2);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDateTime.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals("[2014-09-09T10:00, 2014-09-09T11:00]", booleanOperation.getArg(1).toString());
        assertEquals(Ops.NOT_IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateTime_NotATimeArgument() {
        String selector = "age";
        String argument = "FE";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        thrown.expect(DateTimeParseException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(LocalDateTime.class, selector)));

    }

    private QuerydslFilterParam createFilterParam(Class<? extends Comparable> numberClass, String... pathSelectors) {
        QuerydslFilterParam querydslFilterParam = new QuerydslFilterParam();
        Map<String, Path> mapping = Maps.newHashMap();
        for (String pathSelector : pathSelectors)
            mapping.put(pathSelector, new DateTimePath(numberClass, pathSelector));
        querydslFilterParam.setMapping(mapping);
        return querydslFilterParam;

    }
}
