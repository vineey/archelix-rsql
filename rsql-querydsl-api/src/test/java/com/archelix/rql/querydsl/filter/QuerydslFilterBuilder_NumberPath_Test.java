package com.archelix.rql.querydsl.filter;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.archelix.rql.querydsl.filter.util.RSQLUtil;
import com.archelix.rql.querydsl.util.PathTestUtil;
import com.google.common.collect.Maps;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.NumberPath;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Map;

import static com.archelix.rql.filter.FilterManager.withBuilderAndParam;
import static org.junit.Assert.*;

/**
 * @author vrustia on 9/27/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_NumberPath_Test {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testParse_NumberEquals() {
        String selector = "age";
        String argument = "18";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(Long.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, booleanOperation.getArg(1).toString());
        assertEquals(Ops.EQ, booleanOperation.getOperator());
    }

    @Test
    public void testParse_NumberNotEquals() {
        String selector = "age";
        String argument = "18";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.NOT_EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(Long.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, booleanOperation.getArg(1).toString());
        assertEquals(Ops.NE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_NumberGreaterThan() {
        String selector = "age";
        String argument = "18";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.GREATER_THAN, argument);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(Long.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, booleanOperation.getArg(1).toString());
        assertEquals(Ops.GT, booleanOperation.getOperator());
    }

    @Test
    public void testParse_NumberGreaterThanOrEquals() {
        String selector = "age";
        String argument = "18";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.GREATER_THAN_OR_EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(Long.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, booleanOperation.getArg(1).toString());
        assertEquals(Ops.GOE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_NumberLessThan() {
        String selector = "age";
        String argument = "18";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.LESS_THAN, argument);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(Long.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, booleanOperation.getArg(1).toString());
        assertEquals(Ops.LT, booleanOperation.getOperator());
    }

    @Test
    public void testParse_NumberLessThanOrEquals() {
        String selector = "age";
        String argument = "18";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.LESS_THAN_OR_EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(Long.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(argument, booleanOperation.getArg(1).toString());
        assertEquals(Ops.LOE, booleanOperation.getOperator());
    }


    @Test
    public void testParse_NumberIn() {
        String selector = "id";
        String argument = "18";
        String argument2 = "13";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.IN, argument, argument2);
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(Long.class, selector)));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals(selector, booleanOperation.getArg(0).toString());
        assertEquals(PathTestUtil.pathArg(argument, argument2), booleanOperation.getArg(1).toString());
        assertEquals(Ops.IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_Number_NotANumberArgument() {
        String selector = "age";
        String argument = "FE";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();

        thrown.expect(NumberFormatException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(Long.class, selector)));

    }

    private QuerydslFilterParam createFilterParam(Class<? extends Number> numberClass, String... pathSelectors) {
        QuerydslFilterParam querydslFilterParam = new QuerydslFilterParam();
        Map<String, Path> mapping = Maps.newHashMap();
        for (String pathSelector : pathSelectors)
            mapping.put(pathSelector, new NumberPath(numberClass, pathSelector));
        querydslFilterParam.setMapping(mapping);
        return querydslFilterParam;

    }
}
