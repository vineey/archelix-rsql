/* * MIT License
 *  * Copyright (c) 2016 John Michael Vincent S. Rustia
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
 *  * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
*  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE. *  */
package com.github.vineey.rql.querydsl.filter;

import com.github.vineey.rql.filter.operator.QRSQLOperators;
import com.github.vineey.rql.filter.parser.DefaultFilterParser;
import com.github.vineey.rql.filter.parser.FilterParser;
import com.github.vineey.rql.querydsl.filter.util.RSQLUtil;
import com.github.vineey.rql.querydsl.util.FilterAssertUtil;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanOperation;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.vineey.rql.querydsl.filter.QueryDslFilterContext.withMapping;
import static com.github.vineey.rql.querydsl.util.FilterAssertUtil.withFilterParam;
import static org.junit.Assert.*;

/**
 * @author vrustia on 10/10/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterVisitor_BooleanPath_Test {

    private final static Logger LOG = LoggerFactory.getLogger(QuerydslFilterVisitor_BooleanPath_Test.class);
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testParse_BooleanEquals() {
        String selector = "employed";
        String argument = "false";
        FilterAssertUtil.assertFilter(Boolean.class, selector, RSQLOperators.EQUAL, argument);
    }

    @Test
    public void testParse_NotSupportedOperator() {
        String selector = "employed";
        String argument = "false";
        thrown.expect(UnsupportedRqlOperatorException.class);
        FilterAssertUtil.assertFilter(Boolean.class, selector, QRSQLOperators.SIZE_NOT_EQ, argument);
    }

    @Test
    public void testParse_BooleanIsNull() {
        String selector = "employed";
        String argument = "null";
        String expression = RSQLUtil.build(selector, RSQLOperators.EQUAL, argument);
        DefaultFilterParser defaultFilterParser = new DefaultFilterParser();
        Predicate predicate = defaultFilterParser.parse(expression, withMapping(withFilterParam(Boolean.class, "employed").getMapping()));

        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        assertEquals(1, booleanOperation.getArgs().size());
        assertEquals("employed", booleanOperation.getArg(0).toString());
        assertEquals(Ops.IS_NULL, booleanOperation.getOperator());
    }


    @Test
    public void testParse_BooleanNotEquals() {
        String selector = "employed";
        String argument = "true";
        String[] rqlFilters = RSQLUtil.buildAllSymbols(selector, RSQLOperators.NOT_EQUAL, argument);

        for (String rqlFilter : rqlFilters) {
            LOG.debug("RQL Expression : {}", rqlFilter);
            FilterParser filterParser = new DefaultFilterParser();
            Predicate predicate = filterParser.parse(rqlFilter, withMapping(FilterAssertUtil.buildPathMap(Boolean.class, selector)));
            assertNotNull(predicate);
            assertTrue(predicate instanceof BooleanOperation);
            BooleanOperation booleanOperation = (BooleanOperation) predicate;
            assertEquals(2, booleanOperation.getArgs().size());
            assertEquals(selector + " != " + argument, booleanOperation.getArg(0).toString());
            assertEquals(selector + " is null", booleanOperation.getArg(1).toString());
            assertEquals(Ops.OR, booleanOperation.getOperator());
        }
    }
}
