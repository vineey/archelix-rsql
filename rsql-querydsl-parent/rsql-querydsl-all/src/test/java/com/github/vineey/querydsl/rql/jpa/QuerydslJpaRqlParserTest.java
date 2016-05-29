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
package com.github.vineey.querydsl.rql.jpa;

import com.github.vineey.rql.RqlInput;
import com.github.vineey.rql.querydsl.JpaQuerydslRqlParser;
import com.github.vineey.rql.querydsl.QuerydslMappingParam;
import com.github.vineey.rql.querydsl.QuerydslMappingResult;
import com.github.vineey.rql.querydsl.QuerydslRqlParser;
import com.github.vineey.rql.querydsl.test.jpa.QAccount;
import com.github.vineey.rql.querydsl.test.jpa.QDepartment;
import com.github.vineey.rql.querydsl.test.jpa.QEmployee;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.vineey.rql.querydsl.test.jpa.QEmployee.employee;
import static org.junit.Assert.*;

/**
 * @author vrustia - 4/24/16.
 */
@RunWith(JUnit4.class)
public class QuerydslJpaRqlParserTest {

    public static final QEmployee MANAGER = new QEmployee("manager");
    public static final QAccount MANAGER_ACCOUNT = new QAccount("managerAccount");
    private QuerydslRqlParser querydslRqlParser = new JpaQuerydslRqlParser();
    private Map<EntityPath, EntityPath> JOIN_MAP = ImmutableMap.<EntityPath, EntityPath>builder()
            .put(employee.account, QAccount.account)
            .put(employee.department, QDepartment.department)
            .put(employee.department.manager, MANAGER)
            .put(employee.department.manager.account, MANAGER_ACCOUNT)
            .build();

    @Test
    public void parseRqlInput() {
        String select = "select(employee.number, employee.name.firstname, employee.department.manager.name.firstname, employee.department.manager.account.username)";
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
                .put("employee.name.firstname", QEmployee.employee.name.firstname)
                .put("employee.account.username", QEmployee.employee.account.username)
                .put("employee.department.name", QEmployee.employee.department.name)
                .put("employee.department.manager.name.firstname", QEmployee.employee.department.manager.name.firstname)
                .put("employee.department.manager.account.username", QEmployee.employee.department.manager.account.username)
                .build();

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setRootPath(QEmployee.employee).setPathMapping(pathMapping).setJoinMapping(JOIN_MAP));

        assertSelectExpression(querydslMappingResult);

        assertPredicate(querydslMappingResult);

        assertPage(querydslMappingResult);

        assertSort(querydslMappingResult);

    }

    private void assertSelectExpression(QuerydslMappingResult querydslMappingResult) {
        Expression selectExpression = querydslMappingResult.getProjection();
        assertNotNull(selectExpression);
        assertEquals(Projections.bean(QEmployee.employee, QEmployee.employee.employeeNumber, QEmployee.employee.name.firstname,
                Projections.bean(QDepartment.department,
                        Projections.bean(MANAGER, QEmployee.employee.name.firstname,
                                Projections.bean(MANAGER_ACCOUNT, QAccount.account.username).as(QEmployee.employee.department.manager.account)
                        ).as(QEmployee.employee.department.manager)
                ).as(QEmployee.employee.department)), selectExpression);
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

}
