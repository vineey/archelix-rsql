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
import com.github.vineey.rql.querydsl.filter.util.RSQLUtil;
import com.google.common.collect.Maps;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.NumberPath;
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

import static com.github.vineey.rql.filter.FilterContext.withBuilderAndParam;
import static com.github.vineey.rql.querydsl.util.FilterAssertUtil.assertFilter;

/**
 * @author vrustia on 9/27/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_NumberPath_Test {

    private static final Logger LOG = LoggerFactory.getLogger(QuerydslFilterBuilder_NumberPath_Test.class);
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testParse_NumberEquals() {
        String selector = "age";
        String argument = "18";

        assertFilter(Long.class, selector, RSQLOperators.EQUAL, argument);
    }

    @Test
    public void testParse_NumberNotEquals() {
        String selector = "age";
        String argument = "18";

        assertFilter(Long.class, selector, RSQLOperators.NOT_EQUAL, argument);
    }

    @Test
    public void testParse_NumberGreaterThan() {
        String selector = "age";
        String argument = "18";
        assertFilter(Long.class, selector, RSQLOperators.GREATER_THAN, argument);
    }

    @Test
    public void testParse_NumberGreaterThanOrEquals() {
        String selector = "age";
        String argument = "18";
        assertFilter(Long.class, selector, RSQLOperators.GREATER_THAN_OR_EQUAL, argument);
    }

    @Test
    public void testParse_NumberLessThan() {
        String selector = "age";
        String argument = "18";
        assertFilter(Long.class, selector, RSQLOperators.LESS_THAN, argument);
    }

    @Test
    public void testParse_NumberLessThanOrEquals() {
        String selector = "age";
        String argument = "18";

        assertFilter(Long.class, selector, RSQLOperators.LESS_THAN_OR_EQUAL, argument);
    }

    @Test
    public void testParse_NumberIn() {
        String selector = "id";
        String argument = "18";
        String argument2 = "13";
        assertFilter(Long.class, selector, RSQLOperators.IN, argument, argument2);
    }


    @Test
    public void testParse_NumberNotIn() {
        String selector = "id";
        String argument = "18";
        String argument2 = "13";
        assertFilter(Long.class, selector, RSQLOperators.NOT_IN, argument, argument2);
    }

    @Test
    public void testParse_Number_NotANumberArgument() {
        String selector = "age";
        String argument = "FE";
        String rqlFilter = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);
        FilterParser filterParser = new DefaultFilterParser();
        List<? extends Number> list = new ArrayList<Integer>();
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
