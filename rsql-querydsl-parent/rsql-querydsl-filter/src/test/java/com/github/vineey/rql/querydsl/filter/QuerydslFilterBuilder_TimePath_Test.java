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
package com.github.vineey.rql.querydsl.filter;

import com.github.vineey.rql.filter.operator.QRSQLOperators;
import com.github.vineey.rql.filter.parser.DefaultFilterParser;
import com.github.vineey.rql.filter.parser.FilterParser;
import com.github.vineey.rql.querydsl.filter.converter.UnsupportedFieldClassException;
import com.github.vineey.rql.querydsl.filter.util.DateUtil;
import com.github.vineey.rql.querydsl.util.FilterAssertUtil;
import com.google.common.collect.Maps;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanOperation;
import com.querydsl.core.types.dsl.Expressions;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static com.github.vineey.rql.filter.FilterContext.withBuilderAndParam;
import static com.github.vineey.rql.querydsl.filter.util.RSQLUtil.build;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * @author vrustia on 9/27/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_TimePath_Test {

    private static final Logger LOG = LoggerFactory.getLogger(QuerydslFilterBuilder_TimePath_Test.class);
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testParse_TimeEquals_NullValue() {
        String selector = "startTime";
        String argument = "null";
        for (ComparisonOperator comparisonOperator : asList(RSQLOperators.EQUAL, RSQLOperators.NOT_EQUAL)) {
            String rqlFilter = build(selector, comparisonOperator, argument);

            LOG.debug("RQL Expression : {}", rqlFilter);
            FilterParser filterParser = new DefaultFilterParser();
            Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
            assertNotNull(predicate);
            assertTrue(predicate instanceof BooleanOperation);
            BooleanOperation booleanOperation = (BooleanOperation) predicate;
            assertEquals(1, booleanOperation.getArgs().size());
            assertEquals(selector, booleanOperation.getArg(0).toString());
            assertEquals(RSQLOperators.EQUAL.equals(comparisonOperator) ? Ops.IS_NULL : Ops.IS_NOT_NULL, booleanOperation.getOperator());
        }

    }

    @Test
    public void testParse_TimeOtherOperator_NullValue() {
        String selector = "startTime";
        String argument = null;
        for (ComparisonOperator comparisonOperator : asList(RSQLOperators.GREATER_THAN, RSQLOperators.GREATER_THAN_OR_EQUAL,
                RSQLOperators.LESS_THAN, RSQLOperators.LESS_THAN_OR_EQUAL, RSQLOperators.IN, RSQLOperators.NOT_IN)) {
            try {
                build(selector, comparisonOperator, argument);
            } catch (Exception e) {
                assertEquals(IllegalArgumentException.class, e.getClass());
            }
        }

    }

    @Test
    public void testParse_TimeEquals_EmptyValue() {
        String selector = "startTime";
        for (ComparisonOperator comparisonOperator : asList(RSQLOperators.GREATER_THAN, RSQLOperators.GREATER_THAN_OR_EQUAL,
                RSQLOperators.LESS_THAN, RSQLOperators.LESS_THAN_OR_EQUAL, RSQLOperators.IN, RSQLOperators.NOT_IN)) {
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
        String rqlFilter = build(selector, RSQLOperators.EQUAL, argument);

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
        String rqlFilter = build(selector, RSQLOperators.EQUAL, argument);

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
        String rqlFilter = build(selector, RSQLOperators.NOT_EQUAL, argument);

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
        String rqlFilter = build(selector, RSQLOperators.GREATER_THAN, argument);

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
        String rqlFilter = build(selector, RSQLOperators.GREATER_THAN_OR_EQUAL, argument);

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
        String rqlFilter = build(selector, RSQLOperators.LESS_THAN, argument);

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
        String rqlFilter = build(selector, RSQLOperators.LESS_THAN_OR_EQUAL, argument);

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
        String rqlFilter = build(selector, RSQLOperators.IN, argument, argument2);

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
        String rqlFilter = build(selector, RSQLOperators.NOT_IN, argument, argument2);

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
    public void testParse_UnsupportedOperation() {
        String selector = "startTime";
        String argument = "10:00:00";
        String rqlFilter = build(selector, QRSQLOperators.SIZE_EQ, argument);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        thrown.expect(UnsupportedRqlOperatorException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalTime.class, selector)));
    }

    @Test
    public void testParse_Time_UnsupportedDataType() {
        String selector = "age";
        String argument = "'11:00:00'";
        String rqlFilter = selector + RSQLOperators.EQUAL + argument;
        FilterParser filterParser = new DefaultFilterParser();
        thrown.expect(UnsupportedFieldClassException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(CustomTime.class, selector)));
    }

    @Test
    public void testParse_Time_NotATimeArgument() {
        String selector = "age";
        String argument = "FE";
        String rqlFilter = build(selector, RSQLOperators.EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        thrown.expect(DateTimeParseException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(LocalTime.class, selector)));
    }

    private QuerydslFilterParam createFilterParam(Class<? extends Comparable> numberClass, String... pathSelectors) {
        QuerydslFilterParam querydslFilterParam = new QuerydslFilterParam();
        Map<String, Path> mapping = Maps.newHashMap();
        for (String pathSelector : pathSelectors)
            mapping.put(pathSelector, Expressions.timePath(numberClass, pathSelector));
        querydslFilterParam.setMapping(mapping);
        return querydslFilterParam;

    }
}
