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

import com.github.vineey.rql.filter.parser.DefaultFilterParser;
import com.github.vineey.rql.filter.parser.FilterParser;
import com.github.vineey.rql.querydsl.filter.util.RSQLUtil;
import com.github.vineey.rql.querydsl.util.FilterAssertUtil;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.vineey.rql.querydsl.filter.QueryDslFilterContextUtil.withMapping;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author vrustia on 10/10/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_BooleanPath_Test {

    private final static Logger LOG = LoggerFactory.getLogger(QuerydslFilterBuilder_BooleanPath_Test.class);

    @Test
    public void testParse_BooleanEquals() {
        String selector = "employed";
        String argument = "false";
        FilterAssertUtil.assertFilter(Boolean.class, selector, RSQLOperators.EQUAL, argument);
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
            Assert.assertEquals(2, booleanOperation.getArgs().size());
            Assert.assertEquals(selector + " != " + argument, booleanOperation.getArg(0).toString());
            Assert.assertEquals(selector + " is null", booleanOperation.getArg(1).toString());
            Assert.assertEquals(Ops.OR, booleanOperation.getOperator());
        }
    }
}
