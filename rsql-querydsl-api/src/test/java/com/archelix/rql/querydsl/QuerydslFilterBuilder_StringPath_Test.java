package com.archelix.rql.querydsl;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.google.common.collect.Maps;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.StringPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;

import static com.archelix.rql.filter.FilterManager.withBuilderAndParam;
import static org.junit.Assert.*;

/**
 * @author vrustia on 9/26/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_StringPath_Test {

    @Test
    public void testParse_StringEquals() {
        String rqlFilter = "name==KHIEL";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals("KHIEL", booleanOperation.getArg(1).toString());
        assertEquals(Ops.EQ_IGNORE_CASE, booleanOperation.getOperator());

    }

    @Test
    public void testParse_StringNotEquals() {
        String rqlFilter = "name!=KHIEL";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("!(eqIc(name,KHIEL))", booleanOperation.getArg(0).toString());
        assertEquals("name is null", booleanOperation.getArg(1).toString());
        assertEquals(Ops.OR, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringIn() {
        String rqlFilter = "name=in=(KHIEL,VHIA)";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals(Ops.IN, booleanOperation.getOperator());
        assertEquals("[KHIEL, VHIA]", booleanOperation.getArg(1).toString());
    }

    @Test
    public void testParse_StringNotIn() {
        String rqlFilter = "name=out=(KHIEL,VHIA)";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals(Ops.NOT_IN, booleanOperation.getOperator());
        assertEquals("[KHIEL, VHIA]", booleanOperation.getArg(1).toString());
    }

    @Test
    public void testParse_StringNull() {
        String rqlFilter = "name==NULL";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals(Ops.IS_NULL, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringNotNull() {
        String rqlFilter = "name!=NULL";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals(Ops.IS_NOT_NULL, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringStartsWith() {
        String rqlFilter = "name==Khi*";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals("Khi", booleanOperation.getArg(1).toString());
        assertEquals(Ops.STARTS_WITH_IC, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringEndsWith() {
        String rqlFilter = "name==*Khi";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals("Khi", booleanOperation.getArg(1).toString());
        assertEquals(Ops.ENDS_WITH_IC, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringContainsWith() {
        String rqlFilter = "name==*Khi*";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals("Khi", booleanOperation.getArg(1).toString());
        assertEquals(Ops.STRING_CONTAINS_IC, booleanOperation.getOperator());
    }


    @Test
    public void testParse_StringNotStartsWith() {
        String rqlFilter = "name!=Khi*";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("!startsWithIgnoreCase(name,Khi)", booleanOperation.getArg(0).toString());
        assertEquals("name is null", booleanOperation.getArg(1).toString());
        assertEquals(Ops.OR, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringNotEndsWith() {
        String rqlFilter = "name!=*Khi";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("!endsWithIgnoreCase(name,Khi)", booleanOperation.getArg(0).toString());
        assertEquals("name is null", booleanOperation.getArg(1).toString());
        assertEquals(Ops.OR, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringNotContainsWith() {
        String rqlFilter = "name!=*Khi*";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam()));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals("!containsIc(name,Khi)", booleanOperation.getArg(0).toString());
        assertEquals("name is null", booleanOperation.getArg(1).toString());
        assertEquals(Ops.OR, booleanOperation.getOperator());
    }

    private QuerydslFilterParam createFilterParam() {
        QuerydslFilterParam querydslFilterParam = new QuerydslFilterParam();
        HashMap<String, Path> mapping = Maps.newHashMap();
        mapping.put("name", new StringPath("name"));
        querydslFilterParam.setMapping(mapping);
        return querydslFilterParam;
    }
}