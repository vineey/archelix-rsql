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
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.PredicateOperation;
import com.mysema.query.types.expr.BooleanOperation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import static com.github.vineey.rql.filter.FilterContext.withBuilderAndParam;
import static com.github.vineey.rql.querydsl.util.FilterAssertUtil.withFilterParam;
import static org.junit.Assert.*;

/**
 * @author vrustia - 3/25/16.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_Multiple_Test {

    private final static FilterParser FILTER_PARSER = new DefaultFilterParser();

    @Test
    public void multipleFilters() {

        String rqlFilter = "(name=='Khiel' and birthDate > '2014-05-11') or (name=='Vhia' and birthDate > '2011-09-14')";

        Predicate predicate = FILTER_PARSER.parse(rqlFilter,
                withBuilderAndParam(new QuerydslFilterBuilder(),
                        withFilterParam(new LinkedHashMap.SimpleEntry<>("name", String.class),
                                new LinkedHashMap.SimpleEntry<>("birthDate", LocalDate.class))));

        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        List<Expression<?>> outerArguments = booleanOperation.getArgs();
        assertEquals(2, outerArguments.size());
        Assert.assertEquals(Ops.OR, booleanOperation.getOperator());

        Expression<?> leftSideExpression = outerArguments.get(0);
        assertNotNull(leftSideExpression instanceof PredicateOperation);
        Predicate khielExpression = (PredicateOperation) leftSideExpression;
        Assert.assertEquals("eqIc(name,Khiel) && birthDate > 2014-05-11", khielExpression.toString());

        Expression<?> rightSideExpression = outerArguments.get(1);
        assertNotNull(rightSideExpression instanceof PredicateOperation);
        Predicate vhiaExpression = (PredicateOperation) rightSideExpression;
        Assert.assertEquals("eqIc(name,Vhia) && birthDate > 2011-09-14", vhiaExpression.toString());
    }

}
