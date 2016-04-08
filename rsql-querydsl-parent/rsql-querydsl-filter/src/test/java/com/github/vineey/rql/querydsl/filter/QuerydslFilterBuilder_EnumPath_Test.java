package com.github.vineey.rql.querydsl.filter;

import com.github.vineey.rql.filter.parser.DefaultFilterParser;
import com.github.vineey.rql.filter.parser.FilterParser;
import com.github.vineey.rql.querydsl.filter.util.RSQLUtil;
import com.github.vineey.rql.querydsl.util.PathTestUtil;
import com.google.common.collect.Maps;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.EnumPath;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;

import static com.github.vineey.rql.filter.FilterContext.withBuilderAndParam;
import static com.github.vineey.rql.querydsl.filter.converter.ConverterConstant.NULL;
import static org.junit.Assert.*;

/**
 * @author vrustia on 9/26/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_EnumPath_Test {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testParse_EnumEquals() {
        String status = "status";
        String argument = "ACTIVE";
        String rqlFilter = RSQLUtil.build(status, RSQLOperators.EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(status)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(status, booleanOperation.getArg(0).toString());
        Assert.assertEquals(argument, booleanOperation.getArg(1).toString());
        Assert.assertEquals(Ops.EQ, booleanOperation.getOperator());
    }

    @Test
    public void testParse_EnumIsNull() {
        String status = "status";
        String rqlFilter = RSQLUtil.build(status, RSQLOperators.EQUAL, NULL);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(status)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        Assert.assertEquals(1, booleanOperation.getArgs().size());
        Assert.assertEquals(status, booleanOperation.getArg(0).toString());
        Assert.assertEquals(Ops.IS_NULL, booleanOperation.getOperator());
    }

    @Test
    public void testParse_EnumIsNotNull() {
        String status = "status";
        String rqlFilter = RSQLUtil.build(status, RSQLOperators.NOT_EQUAL, NULL);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(status)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        Assert.assertEquals(1, booleanOperation.getArgs().size());
        Assert.assertEquals(status, booleanOperation.getArg(0).toString());
        Assert.assertEquals(Ops.IS_NOT_NULL, booleanOperation.getOperator());
    }

    @Test
    public void testParse_EnumNotEquals() {
        String status = "status";
        String argument = "ACTIVE";
        String rqlFilter = RSQLUtil.build(status, RSQLOperators.NOT_EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(status)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(status, booleanOperation.getArg(0).toString());
        Assert.assertEquals(argument, booleanOperation.getArg(1).toString());
        Assert.assertEquals(Ops.NE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_EnumIn() {
        String status = "status";
        String argument = "ACTIVE";
        String argument2 = "PENDING";
        String rqlFilter = RSQLUtil.build(status, RSQLOperators.IN, argument, argument2);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(status)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(status, booleanOperation.getArg(0).toString());
        Assert.assertEquals(PathTestUtil.pathArg(argument, argument2), booleanOperation.getArg(1).toString());
        Assert.assertEquals(Ops.IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_EnumNotIn() {
        String status = "status";
        String argument = "ACTIVE";
        String argument2 = "PENDING";
        String rqlFilter = RSQLUtil.build(status, RSQLOperators.NOT_IN, argument, argument2);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(status)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        Assert.assertEquals(2, booleanOperation.getArgs().size());
        Assert.assertEquals(status, booleanOperation.getArg(0).toString());
        Assert.assertEquals(PathTestUtil.pathArg(argument, argument2), booleanOperation.getArg(1).toString());
        Assert.assertEquals(Ops.NOT_IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_EnumUnsupportedValue() {
        String status = "status";
        String argument = "UNKNOWN";
        String rqlFilter = RSQLUtil.build(status, RSQLOperators.EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();

        thrown.expect(IllegalArgumentException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(status)));
    }

    @Test
    public void testParse_EnumUnsupportedRqlOperator() {
        String selector = "status";
        String argument = "ACTIVE";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.GREATER_THAN_OR_EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();

        thrown.expect(UnsupportedRqlOperatorException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(selector)));
    }

    enum Status {
        ACTIVE,
        PENDING,
        INACTIVE
    }

    private QuerydslFilterParam createFilterParam(String... pathSelectors) {
        QuerydslFilterParam querydslFilterParam = new QuerydslFilterParam();
        HashMap<String, Path> mapping = Maps.newHashMap();
        for (String pathSelector : pathSelectors)
            mapping.put(pathSelector, new EnumPath(Status.class, pathSelector));
        querydslFilterParam.setMapping(mapping);
        return querydslFilterParam;
    }

}