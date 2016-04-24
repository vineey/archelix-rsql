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
import com.github.vineey.rql.querydsl.test.QEmployee;
import com.google.common.collect.ImmutableMap;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.*;
import com.mysema.query.types.expr.BooleanOperation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author vrustia - 4/24/16.
 */
@RunWith(JUnit4.class)
public class QuerydslRqlParserTest {

    QuerydslRqlParser querydslRqlParser = new DefaultQuerydslRqlParser();
    @Test
    public void parseRqlInput(){
        String rqlFilter = "(employee.number=='1' and employee.names =size= 1) or (employee.number=='2'  and employee.names =size= 2)";
        String limit = "limit(0, 10)";
        String sort = "sort(+employee.number)";
        RqlInput rqlInput = new RqlInput()
                .setFilter(rqlFilter)
                .setLimit(limit)
                .setSort(sort);

        Map<String , Path> pathMapping = ImmutableMap.<String, Path>builder()
                .put("employee.number", QEmployee.employee.employeeNumber)
                .put("employee.names", QEmployee.employee.names)
                .build();

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setPathMapping(pathMapping));

        assertPredicate(querydslMappingResult);

        assertPage(querydslMappingResult);

        assertSort(querydslMappingResult);
    }

    private void assertSort(QuerydslMappingResult querydslMappingResult) {
        List<OrderSpecifier> orderSpecifiers = querydslMappingResult.getOrderSpecifiers();
        assertEquals(1, orderSpecifiers.size());
        OrderSpecifier orderSpecifier = orderSpecifiers.get(0);
        assertEquals(Order.ASC, orderSpecifier.getOrder());
        assertEquals(QEmployee.employee.employeeNumber, orderSpecifier.getTarget());
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
        Assert.assertEquals(Ops.OR, booleanOperation.getOperator());

        Expression<?> leftSideExpression = outerArguments.get(0);
        assertNotNull(leftSideExpression instanceof PredicateOperation);
        Predicate khielExpression = (PredicateOperation) leftSideExpression;
        Assert.assertEquals(QEmployee.employee.employeeNumber.equalsIgnoreCase("1").and(QEmployee.employee.names.size().eq(1)).toString(), khielExpression.toString());

        Expression<?> rightSideExpression = outerArguments.get(1);
        assertNotNull(rightSideExpression instanceof PredicateOperation);
        Predicate vhiaExpression = (PredicateOperation) rightSideExpression;
        Assert.assertEquals(QEmployee.employee.employeeNumber.equalsIgnoreCase("2").and(QEmployee.employee.names.size().eq(2)).toString(), vhiaExpression.toString());
    }
}
