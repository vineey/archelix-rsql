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
 package com.github.vineey.rql.querydsl.jpa.filter;

import com.github.vineey.rql.filter.operator.QRSQLOperators;
import com.github.vineey.rql.filter.parser.DefaultFilterParser;
import com.github.vineey.rql.filter.parser.FilterParser;
import com.github.vineey.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.github.vineey.rql.querydsl.filter.util.RSQLUtil;
import com.github.vineey.rql.querydsl.jpa.util.FilterAssertUtil;
import com.google.common.collect.Maps;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.vineey.rql.querydsl.jpa.filter.JpaQuerydslFilterContext.withBuilderAndParam;

/**
 * @author vrustia on 9/27/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterVisitor_NumberPath_Test {

    private static final Logger LOG = LoggerFactory.getLogger(QuerydslFilterVisitor_NumberPath_Test.class);
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testParse_NumberEquals() {
        String selector = "age";
        String argument = "18";

        FilterAssertUtil.assertFilter(Long.class, selector, RSQLOperators.EQUAL, argument);
    }

    @Test
    public void testParse_NumberNotEquals() {
        String selector = "age";
        String argument = "18";

        FilterAssertUtil.assertFilter(Long.class, selector, RSQLOperators.NOT_EQUAL, argument);
    }

    @Test
    public void testParse_NumberGreaterThan() {
        String selector = "age";
        String argument = "18";
        FilterAssertUtil.assertFilter(Long.class, selector, RSQLOperators.GREATER_THAN, argument);
    }

    @Test
    public void testParse_NumberGreaterThanOrEquals() {
        String selector = "age";
        String argument = "18";
        FilterAssertUtil.assertFilter(Long.class, selector, RSQLOperators.GREATER_THAN_OR_EQUAL, argument);
    }

    @Test
    public void testParse_NumberLessThan() {
        String selector = "age";
        String argument = "18";
        FilterAssertUtil.assertFilter(Long.class, selector, RSQLOperators.LESS_THAN, argument);
    }

    @Test
    public void testParse_NumberLessThanOrEquals() {
        String selector = "age";
        String argument = "18";

        FilterAssertUtil.assertFilter(Long.class, selector, RSQLOperators.LESS_THAN_OR_EQUAL, argument);
    }

    @Test
    public void testParse_NumberIn() {
        String selector = "id";
        String argument = "18";
        String argument2 = "13";
        FilterAssertUtil.assertFilter(Long.class, selector, RSQLOperators.IN, argument, argument2);
    }


    @Test
    public void testParse_NumberNotIn() {
        String selector = "id";
        String argument = "18";
        String argument2 = "13";
        FilterAssertUtil.assertFilter(Long.class, selector, RSQLOperators.NOT_IN, argument, argument2);
    }

    @Test
    public void testParse_Number_NotANumberArgument() {
        String selector = "age";
        String argument = "FE";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        List<? extends Number> list = new ArrayList<Integer>();
        thrown.expect(NumberFormatException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new JpaQuerydslFilterVisitor(), createFilterParam(Long.class, selector)));

    }

    @Test
    public void testParse_Number_NotSupportedNumber() {
        String selector = "age";
        String argument = "1";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        thrown.expect(UnsupportedOperationException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new JpaQuerydslFilterVisitor(), createFilterParam(CustomNumber.class, selector)));

    }

    @Test
    public void testParse_Number_NotSupportedOperator() {
        String selector = "age";
        String argument = "1";
        String rqlFilter = selector + QRSQLOperators.SIZE_EQ + argument;
        FilterParser filterParser = new DefaultFilterParser();
        thrown.expect(UnsupportedRqlOperatorException.class);
        filterParser.parse(rqlFilter, withBuilderAndParam(new JpaQuerydslFilterVisitor(), createFilterParam(Long.class, selector)));

    }

    private <T extends Number & Comparable<?>> JpaQuerydslFilterParam createFilterParam(Class<T> numberClass, String... pathSelectors) {
        JpaQuerydslFilterParam querydslFilterParam = new JpaQuerydslFilterParam();
        Map<String, Path> mapping = Maps.newHashMap();
        for (String pathSelector : pathSelectors) {
            mapping.put(pathSelector, Expressions.numberPath(numberClass, pathSelector));
        }
        querydslFilterParam.setMapping(mapping);
        return querydslFilterParam;

    }
}
