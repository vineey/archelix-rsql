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
package com.github.vineey.querydsl.rql.mongo;

import com.github.vineey.rql.RqlInput;
import com.github.vineey.rql.querydsl.MongoQuerydslMappingParam;
import com.github.vineey.rql.querydsl.MongoQuerydslMappingResult;
import com.github.vineey.rql.querydsl.MongoQuerydslRqlParser;
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

import static org.junit.Assert.*;

/**
 * @author vrustia - 5/29/16.
 */
@RunWith(JUnit4.class)
public class QuerydslMongoRqlParserTest {

    private MongoQuerydslRqlParser querydslRqlParser = new MongoQuerydslRqlParser();

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

        MongoQuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new MongoQuerydslMappingParam().setRootPath(QContactDocument.contactDocument).setPathMapping(pathMapping));

        assertMongoSelectExpression(querydslMappingResult);

        assertNotNull(querydslMappingResult);

        assertMongoPredicate(querydslMappingResult);

        assertMongoPage(querydslMappingResult);

        assertMongoSort(querydslMappingResult);
    }

    private void assertMongoSelectExpression(MongoQuerydslMappingResult querydslMappingResult) {
        Expression selectExpression = querydslMappingResult.getProjection();
        assertNotNull(selectExpression);
        assertEquals(Projections.bean(QContactDocument.contactDocument, QContactDocument.contactDocument.name, QContactDocument.contactDocument.age), selectExpression);
    }

    private void assertMongoSort(MongoQuerydslMappingResult querydslMappingResult) {
        List<OrderSpecifier> orderSpecifiers = querydslMappingResult.getOrderSpecifiers();
        assertEquals(1, orderSpecifiers.size());
        OrderSpecifier orderSpecifier = orderSpecifiers.get(0);
        assertEquals(Order.ASC, orderSpecifier.getOrder());
        assertEquals(QContactDocument.contactDocument.name, orderSpecifier.getTarget());
    }

    private void assertMongoPage(MongoQuerydslMappingResult querydslMappingResult) {
        QueryModifiers page = querydslMappingResult.getPage();
        assertEquals(0, page.getOffset().longValue());
        assertEquals(10, page.getLimit().longValue());
    }

    private void assertMongoPredicate(MongoQuerydslMappingResult querydslMappingResult) {
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
