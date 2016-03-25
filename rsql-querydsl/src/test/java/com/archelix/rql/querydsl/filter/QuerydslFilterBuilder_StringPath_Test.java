package com.archelix.rql.querydsl.filter;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.archelix.rql.querydsl.filter.util.RSQLUtil;
import com.google.common.collect.Maps;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.StringPath;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testParse_StringEquals() {
        String rqlFilter = "name==KHIEL";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals("KHIEL", booleanOperation.getArg(1).toString());
        assertEquals(Ops.EQ_IGNORE_CASE, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringNotEquals() {
        String rqlFilter = "name!=KHIEL";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("!(eqIc(name,KHIEL))", booleanOperation.getArg(0).toString());
        assertEquals("name is null", booleanOperation.getArg(1).toString());
        assertEquals(Ops.OR, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringIn() {
        String rqlFilter = "name=in=(KHIEL,VHIA)";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals("[KHIEL, VHIA]", booleanOperation.getArg(1).toString());
        assertEquals(Ops.IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringNotIn() {
        String rqlFilter = "name=out=(KHIEL,VHIA)";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals("[KHIEL, VHIA]", booleanOperation.getArg(1).toString());
        assertEquals(Ops.NOT_IN, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringNull() {
        String rqlFilter = "name==NULL";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(1, booleanOperation.getArgs().size());
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals(Ops.IS_NULL, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringNotNull() {
        String rqlFilter = "name!=NULL";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(1, booleanOperation.getArgs().size());
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals(Ops.IS_NOT_NULL, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringStartsWith() {
        String rqlFilter = "name==Khi*";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals("Khi", booleanOperation.getArg(1).toString());
        assertEquals(Ops.STARTS_WITH_IC, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringEndsWith() {
        String rqlFilter = "name==*Khi";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals("Khi", booleanOperation.getArg(1).toString());
        assertEquals(Ops.ENDS_WITH_IC, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringContainsWith() {
        String rqlFilter = "name==*Khi*";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("name", booleanOperation.getArg(0).toString());
        assertEquals("Khi", booleanOperation.getArg(1).toString());
        assertEquals(Ops.STRING_CONTAINS_IC, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringNotStartsWith() {
        String rqlFilter = "name!=Khi*";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("!startsWithIgnoreCase(name,Khi)", booleanOperation.getArg(0).toString());
        assertEquals("name is null", booleanOperation.getArg(1).toString());
        assertEquals(Ops.OR, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringNotEndsWith() {
        String rqlFilter = "name!=*Khi";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("!endsWithIgnoreCase(name,Khi)", booleanOperation.getArg(0).toString());
        assertEquals("name is null", booleanOperation.getArg(1).toString());
        assertEquals(Ops.OR, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringNotContainsWith() {
        String rqlFilter = "name!=*Khi*";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("!containsIc(name,Khi)", booleanOperation.getArg(0).toString());
        assertEquals("name is null", booleanOperation.getArg(1).toString());
        assertEquals(Ops.OR, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringAnd() {
        String rqlFilter = "name==KHIEL;familyName=='Dela Cruz'";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name", "familyName")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("eqIc(name,KHIEL)", booleanOperation.getArg(0).toString());
        assertEquals("eqIc(familyName,Dela Cruz)", booleanOperation.getArg(1).toString());
        assertEquals(Ops.AND, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringAnd_Multiple() {
        String rqlFilter = "firstName==KHIEL;familyName==Rustia;middleName==Laid";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("firstName", "middleName", "familyName")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("eqIc(firstName,KHIEL) && eqIc(familyName,Rustia)", booleanOperation.getArg(0).toString());
        assertEquals("eqIc(middleName,Laid)", booleanOperation.getArg(1).toString());
        assertEquals(Ops.AND, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringOr() {
        String rqlFilter = "name==KHIEL,familyName=='Dela Cruz'";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("name", "familyName")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("eqIc(name,KHIEL)", booleanOperation.getArg(0).toString());
        assertEquals("eqIc(familyName,Dela Cruz)", booleanOperation.getArg(1).toString());
        assertEquals(Ops.OR, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringOr_Multiple() {
        String rqlFilter = "firstName==KHIEL,familyName==Rustia,middleName==Laid";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("firstName", "middleName", "familyName")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("eqIc(firstName,KHIEL) || eqIc(familyName,Rustia)", booleanOperation.getArg(0).toString());
        assertEquals("eqIc(middleName,Laid)", booleanOperation.getArg(1).toString());
        assertEquals(Ops.OR, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringInnerAnd_OuterOr_Multiple() {
        String rqlFilter = "(firstName==KHIEL;familyName==Rustia),middleName==Laid";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("firstName", "middleName", "familyName")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("eqIc(firstName,KHIEL) && eqIc(familyName,Rustia)", booleanOperation.getArg(0).toString());
        assertEquals("eqIc(middleName,Laid)", booleanOperation.getArg(1).toString());
        assertEquals(Ops.OR, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringOuterAnd_InnerOr_Multiple() {
        String rqlFilter = "firstName==KHIEL;(familyName==Rustia,middleName==Laid)";
        FilterParser filterParser = new DefaultFilterParser();
        Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam("firstName", "middleName", "familyName")));
        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;
        assertEquals(2, booleanOperation.getArgs().size());
        assertEquals("eqIc(firstName,KHIEL)", booleanOperation.getArg(0).toString());
        assertEquals("eqIc(familyName,Rustia) || eqIc(middleName,Laid)", booleanOperation.getArg(1).toString());
        assertEquals(Ops.AND, booleanOperation.getOperator());
    }

    @Test
    public void testParse_StringUnsupportedRqlOperator() {
        String selector = "status";
        String argument = "ACTIVE";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.GREATER_THAN_OR_EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();

        thrown.expect(UnsupportedRqlOperatorException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), createFilterParam(selector)));
    }

    private QuerydslFilterParam createFilterParam(String... pathSelectors) {
        QuerydslFilterParam querydslFilterParam = new QuerydslFilterParam();
        HashMap<String, Path> mapping = Maps.newHashMap();
        for (String pathSelector : pathSelectors)
            mapping.put(pathSelector, new StringPath(pathSelector));
        querydslFilterParam.setMapping(mapping);
        return querydslFilterParam;
    }
}