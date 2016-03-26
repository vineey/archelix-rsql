package com.archelix.rql.querydsl.filter;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.archelix.rql.querydsl.filter.util.DateUtil;
import com.archelix.rql.querydsl.filter.util.RSQLUtil;
import com.archelix.rql.querydsl.util.FilterAssertUtil;
import com.archelix.rql.querydsl.util.PathTestUtil;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import java.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeParseException;

import static com.archelix.rql.filter.FilterContext.withBuilderAndParam;
import static org.junit.Assert.*;

/**
 * @author vrustia on 9/27/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_DatePath_Test {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final Logger LOG = LoggerFactory.getLogger(QuerydslFilterBuilder_DatePath_Test.class);
    public static final FilterParser FILTER_PARSER = new DefaultFilterParser();

    private String formatLocalDate(BooleanOperation booleanOperation) {
        return DateUtil.formatLocalDate(getLocalDateArg(booleanOperation));
    }

    private LocalDate getLocalDateArg(BooleanOperation booleanOperation) {
        return (LocalDate) ((ConstantImpl) booleanOperation.getArg(1)).getConstant();
    }

    @Test
    public void testParse_DateEquals_AM() {
        String selector = "startTime";
        String argument = "'2014-09-09'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        Predicate predicate = FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        assertEquals(Ops.EQ, booleanOperation.getOperator());

    }

    @Test
    public void testParse_DateEquals_PM() {
        String selector = "startTime";
        String argument = "'2014-09-09'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        Predicate predicate = FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        assertEquals(Ops.EQ, booleanOperation.getOperator());

    }

    @Test
    public void testParse_DateNotEquals() {
        String selector = "startTime";
        String argument = "'2014-09-09'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.NOT_EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        Predicate predicate = FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        assertEquals(Ops.NE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateGreaterThan() {
        String selector = "startTime";
        String argument = "'2014-09-09'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.GREATER_THAN, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        Predicate predicate = FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        assertEquals(Ops.GT, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateGreaterThanOrEquals() {
        String selector = "startTime";
        String argument = "'2014-09-09'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.GREATER_THAN_OR_EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        Predicate predicate = FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        assertEquals(Ops.GOE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateLessThan() {
        String selector = "startTime";
        String argument = "'2014-09-09'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.LESS_THAN, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        Predicate predicate = FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        assertEquals(Ops.LT, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateLessThanOrEquals() {
        String selector = "startTime";
        String argument = "'2014-09-09'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.LESS_THAN_OR_EQUAL, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        assertEquals(Ops.LOE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateIn() {
        String selector = "startTime";
        String argument = "'2014-09-09'";
        String argument2 = "'2014-09-09'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.IN, argument, argument2);

        LOG.debug("RQL Expression : {}", rqlFilter);
        Predicate predicate = FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals("[2014-09-09, 2014-09-09]", booleanOperation.getArg(1).toString());
        assertEquals(Ops.IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateNotIn() {
        String selector = "startTime";
        String argument = "'2014-09-09'";
        String argument2 = "'2014-09-09'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.NOT_IN, argument, argument2);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals("[2014-09-09, 2014-09-09]", booleanOperation.getArg(1).toString());
        assertEquals(Ops.NOT_IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_Date_NotATimeArgument() {
        String selector = "age";
        String argument = "FE";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);
        thrown.expect(DateTimeParseException.class);
        FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));

    }

}
