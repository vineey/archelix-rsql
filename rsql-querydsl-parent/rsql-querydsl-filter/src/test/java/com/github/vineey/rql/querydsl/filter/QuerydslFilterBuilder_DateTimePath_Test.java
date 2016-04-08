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

import com.github.vineey.rql.filter.parser.DefaultFilterParser;
import com.github.vineey.rql.filter.parser.FilterParser;
import com.github.vineey.rql.querydsl.filter.util.DateUtil;
import com.github.vineey.rql.querydsl.filter.util.RSQLUtil;
import com.github.vineey.rql.querydsl.util.FilterAssertUtil;
import com.github.vineey.rql.querydsl.util.PathTestUtil;
import com.google.common.collect.Maps;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.DateTimePath;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static com.github.vineey.rql.filter.FilterContext.withBuilderAndParam;
import static org.junit.Assert.*;

/**
 * @author vrustia on 9/27/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_DateTimePath_Test {

    private static final Logger LOG = LoggerFactory.getLogger(QuerydslFilterBuilder_DateTimePath_Test.class);
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        Assert.assertEquals(Ops.EQ, booleanOperation.getOperator());

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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        Assert.assertEquals(Ops.EQ, booleanOperation.getOperator());

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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        Assert.assertEquals(Ops.NE, booleanOperation.getOperator());
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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        Assert.assertEquals(Ops.GT, booleanOperation.getOperator());
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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        Assert.assertEquals(Ops.GOE, booleanOperation.getOperator());
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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        Assert.assertEquals(Ops.LT, booleanOperation.getOperator());
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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.unquote(argument), formatLocalDateTime(booleanOperation));
        Assert.assertEquals(Ops.LOE, booleanOperation.getOperator());
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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        Assert.assertEquals("[2014-09-09T10:00, 2014-09-09T11:00]", booleanOperation.getArg(1).toString());
        Assert.assertEquals(Ops.IN, booleanOperation.getOperator());
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
        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(selector, booleanOperation.getArg(0).toString());
        Assert.assertEquals("[2014-09-09T10:00, 2014-09-09T11:00]", booleanOperation.getArg(1).toString());
        Assert.assertEquals(Ops.NOT_IN, booleanOperation.getOperator());
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
