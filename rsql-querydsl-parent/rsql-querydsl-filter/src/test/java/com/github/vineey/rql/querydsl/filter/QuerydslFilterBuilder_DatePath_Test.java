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
import com.github.vineey.rql.querydsl.filter.util.RSQLUtil;
import com.github.vineey.rql.querydsl.util.FilterAssertUtil;
import com.github.vineey.rql.querydsl.util.PathTestUtil;
import com.google.common.collect.Maps;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanOperation;
import com.querydsl.core.types.dsl.Expressions;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static com.github.vineey.rql.filter.FilterContext.withBuilderAndParam;
import static org.junit.Assert.*;

/**
 * @author vrustia on 9/27/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_DatePath_Test {

    public static final FilterParser FILTER_PARSER = new DefaultFilterParser();
    private static final Logger LOG = LoggerFactory.getLogger(QuerydslFilterBuilder_DatePath_Test.class);
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        Assert.assertEquals(Ops.EQ, booleanOperation.getOperator());

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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        Assert.assertEquals(Ops.EQ, booleanOperation.getOperator());

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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        Assert.assertEquals(Ops.NE, booleanOperation.getOperator());
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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        Assert.assertEquals(Ops.GT, booleanOperation.getOperator());
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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        Assert.assertEquals(Ops.GOE, booleanOperation.getOperator());
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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        Assert.assertEquals(Ops.LT, booleanOperation.getOperator());
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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDate(booleanOperation));
        Assert.assertEquals(Ops.LOE, booleanOperation.getOperator());
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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        Assert.assertEquals("[2014-09-09, 2014-09-09]", booleanOperation.getArg(1).toString());
        Assert.assertEquals(Ops.IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_DateNotIn() {
        String selector = "startTime";
        String argument = "'2014-09-01'";
        String argument2 = "'2014-09-09'";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.NOT_IN, argument, argument2);

        LOG.debug("RQL Expression : {}", rqlFilter);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        Assert.assertEquals("[2014-09-01, 2014-09-09]", booleanOperation.getArg(1).toString());
        Assert.assertEquals(Ops.NOT_IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_Date_NotATimeArgument() {
        String selector = "age";
        String argument = "FE";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);
        thrown.expect(DateTimeParseException.class);
        FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));

    }

    @Test
    public void testParse_Date_UnsupportedOperation() {
        String selector = "age";
        String argument = "'2014-09-09'";
        String rqlFilter = selector + QRSQLOperators.SIZE_EQ + argument;
        thrown.expect(UnsupportedRqlOperatorException.class);
        FILTER_PARSER.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(LocalDate.class, selector)));

    }

    @Test
    public void testParse_Time_unSupportedDataType() {
        String selector = "age";
        String argument = "'2014-09-09'";
        String rqlFilter = selector + RSQLOperators.EQUAL + argument;
        FilterParser filterParser = new DefaultFilterParser();
        thrown.expect(UnsupportedFieldClassException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(CustomDate.class, selector)));
    }

    private QuerydslFilterParam createFilterParam(Class<? extends Comparable> numberClass, String... pathSelectors) {
        QuerydslFilterParam querydslFilterParam = new QuerydslFilterParam();
        Map<String, Path> mapping = Maps.newHashMap();
        for (String pathSelector : pathSelectors)
            mapping.put(pathSelector, Expressions.datePath(numberClass, pathSelector));
        querydslFilterParam.setMapping(mapping);
        return querydslFilterParam;

    }
}
