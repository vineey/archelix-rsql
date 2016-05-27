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
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* 
*/
package com.github.vineey.querydsl.rql;

import com.github.vineey.rql.RqlInput;
import com.github.vineey.rql.querydsl.DefaultQuerydslRqlParser;
import com.github.vineey.rql.querydsl.QuerydslMappingParam;
import com.github.vineey.rql.querydsl.QuerydslMappingResult;
import com.github.vineey.rql.querydsl.QuerydslRqlParser;
import com.github.vineey.rql.querydsl.test.jpa.QEmployee;
import com.github.vineey.rql.querydsl.test.mongo.QContactDocument;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author vrustia - 4/24/16.
 */
@RunWith(JUnit4.class)
public class QuerydslRqlParserTest {

    private QuerydslRqlParser querydslRqlParser = new DefaultQuerydslRqlParser();

    @Test
    public void parseRqlInput() {
        String select = "select(employee.number)";
        String rqlFilter = "(employee.number=='1' and employee.names =size= 1) or (employee.number=='2'  and employee.names =size= 2)";
        String limit = "limit(0, 10)";
        String sort = "sort(+employee.number)";
        RqlInput rqlInput = new RqlInput()
                .setSelect(select)
                .setFilter(rqlFilter)
                .setLimit(limit)
                .setSort(sort);

        Map<String, Path> pathMapping = ImmutableMap.<String, Path>builder()
                .put("employee.number", QEmployee.employee.employeeNumber)
                .put("employee.names", QEmployee.employee.names)
                .build();

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setRootPath(QEmployee.employee).setPathMapping(pathMapping));

        assertSelectExpression(querydslMappingResult);

        assertPredicate(querydslMappingResult);

        assertPage(querydslMappingResult);

        assertSort(querydslMappingResult);
    }

    private void assertSelectExpression(QuerydslMappingResult querydslMappingResult) {
        Expression selectExpression = querydslMappingResult.getProjection();
        assertNotNull(selectExpression);
        assertEquals(Projections.bean(QEmployee.employee, QEmployee.employee.employeeNumber), selectExpression);
        Set<Path> selectPaths = querydslMappingResult.getSelectPaths();
        assertNotNull(selectPaths);
        assertEquals(1, selectPaths.size());
    }

    private void assertSort(QuerydslMappingResult querydslMappingResult) {
        List<OrderSpecifier> orderSpecifiers = querydslMappingResult.getOrderSpecifiers();
        assertEquals(1, orderSpecifiers.size());
        OrderSpecifier orderSpecifier = orderSpecifiers.get(0);
        assertEquals(Order.ASC, orderSpecifier.getOrder());
        assertEquals(QEmployee.employee.employeeNumber, orderSpecifier.getTarget());

        Set<Path> sortPaths = querydslMappingResult.getSortPaths();
        assertNotNull(sortPaths);
        assertEquals(1, sortPaths.size());
    }

    private void assertPage(QuerydslMappingResult querydslMappingResult) {
        QueryModifiers page = querydslMappingResult.getPage();
        assertEquals(0, page.getOffset().longValue());
        assertEquals(10, page.getLimit().longValue());
    }

    private void assertPredicate(QuerydslMappingResult querydslMappingResult) {
        Predicate predicate = querydslMappingResult.getPredicate();

        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        List<Expression<?>> outerArguments = booleanOperation.getArgs();
        assertEquals(2, outerArguments.size());
        assertEquals(Ops.OR, booleanOperation.getOperator());

        Expression<?> leftSideExpression = outerArguments.get(0);
        assertNotNull(leftSideExpression instanceof PredicateOperation);
        Predicate khielExpression = (PredicateOperation) leftSideExpression;
        assertEquals(QEmployee.employee.employeeNumber.equalsIgnoreCase("1").and(QEmployee.employee.names.size().eq(1)).toString(), khielExpression.toString());

        Expression<?> rightSideExpression = outerArguments.get(1);
        assertNotNull(rightSideExpression instanceof PredicateOperation);
        Predicate vhiaExpression = (PredicateOperation) rightSideExpression;
        assertEquals(QEmployee.employee.employeeNumber.equalsIgnoreCase("2").and(QEmployee.employee.names.size().eq(2)).toString(), vhiaExpression.toString());


        Set<Path> filterPaths = querydslMappingResult.getFilterPaths();
        assertNotNull(filterPaths);
        assertEquals(2, filterPaths.size());
    }

    @Test
    public void parseMongoRqlInput() {
        String select = "select(contact.name, contact.age)";
        String rqlFilter = "(contact.age =='1' and contact.name == 'A*') or (contact.age > '1'  and contact.bday == '2015-05-05')";
        String limit = "limit(0, 10)";
        String sort = "sort(+contact.name)";

        RqlInput rqlInput = new RqlInput()
                .setSelect(select)
                .setFilter(rqlFilter)
                .setLimit(limit)
                .setSort(sort);

        Map<String, Path> pathMapping = ImmutableMap.<String, Path>builder()
                .put("contact.name", QContactDocument.contactDocument.name)
                .put("contact.age", QContactDocument.contactDocument.age)
                .put("contact.bday", QContactDocument.contactDocument.bday)
                .build();

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setRootPath(QContactDocument.contactDocument).setPathMapping(pathMapping));

        assertMongoSelectExpression(querydslMappingResult);

        assertNotNull(querydslMappingResult);

        assertMongoPredicate(querydslMappingResult);

        assertMongoPage(querydslMappingResult);

        assertMongoSort(querydslMappingResult);
    }

    private void assertMongoSelectExpression(QuerydslMappingResult querydslMappingResult) {
        Expression selectExpression = querydslMappingResult.getProjection();
        assertNotNull(selectExpression);
        assertEquals(Projections.bean(QContactDocument.contactDocument, QContactDocument.contactDocument.name, QContactDocument.contactDocument.age), selectExpression);
    }

    private void assertMongoSort(QuerydslMappingResult querydslMappingResult) {
        List<OrderSpecifier> orderSpecifiers = querydslMappingResult.getOrderSpecifiers();
        assertEquals(1, orderSpecifiers.size());
        OrderSpecifier orderSpecifier = orderSpecifiers.get(0);
        assertEquals(Order.ASC, orderSpecifier.getOrder());
        assertEquals(QContactDocument.contactDocument.name, orderSpecifier.getTarget());
    }

    private void assertMongoPage(QuerydslMappingResult querydslMappingResult) {
        QueryModifiers page = querydslMappingResult.getPage();
        assertEquals(0, page.getOffset().longValue());
        assertEquals(10, page.getLimit().longValue());
    }

    private void assertMongoPredicate(QuerydslMappingResult querydslMappingResult) {
        Predicate predicate = querydslMappingResult.getPredicate();

        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        List<Expression<?>> outerArguments = booleanOperation.getArgs();
        assertEquals(2, outerArguments.size());
        assertEquals(Ops.OR, booleanOperation.getOperator());

        Expression<?> leftSideExpression = outerArguments.get(0);
        assertNotNull(leftSideExpression instanceof PredicateOperation);
        Predicate khielExpression = (PredicateOperation) leftSideExpression;
        assertEquals(QContactDocument.contactDocument.age.eq(1).and(QContactDocument.contactDocument.name.startsWithIgnoreCase("A")).toString(), khielExpression.toString());

        Expression<?> rightSideExpression = outerArguments.get(1);
        assertNotNull(rightSideExpression instanceof PredicateOperation);
        Predicate vhiaExpression = (PredicateOperation) rightSideExpression;
        assertEquals(QContactDocument.contactDocument.age.gt(1).and(QContactDocument.contactDocument.bday.eq(LocalDate.of(2015, 5, 5))).toString(), vhiaExpression.toString());
    }
}
