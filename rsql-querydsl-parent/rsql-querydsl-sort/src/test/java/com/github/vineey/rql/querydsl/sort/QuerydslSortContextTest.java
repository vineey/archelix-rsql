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
package com.github.vineey.rql.querydsl.sort;

import com.github.vineey.rql.querydsl.test.QEmployee;
import com.github.vineey.rql.sort.parser.DefaultSortParser;
import com.google.common.collect.ImmutableMap;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author vrustia - 4/17/16.
 */
@RunWith(JUnit4.class)
public class QuerydslSortContextTest {
    @Test
    public void sortToOrderSpecifier_Ascending() {
        String sortExpression = "sort(+employeeNumber)";
        DefaultSortParser sortParser = new DefaultSortParser();
        Map<String, Path> mappings = ImmutableMap.<String, Path>builder()
                .put("employeeNumber", QEmployee.employee.employeeNumber)
                .build();

        OrderSpecifierList orderSpecifierList = sortParser.parse(sortExpression, QuerydslSortContext.withMapping(mappings));
        assertNotNull(orderSpecifierList);

        List<OrderSpecifier> orderSpecifiers = orderSpecifierList.getOrders();
        assertNotNull(orderSpecifiers);
        assertEquals(1, orderSpecifiers.size());
        OrderSpecifier orderSpecifier = orderSpecifiers.get(0);
        assertEquals(Order.ASC, orderSpecifier.getOrder());
        assertEquals(QEmployee.employee.employeeNumber, orderSpecifier.getTarget());
    }

    @Test
    public void sortToOrderSpecifier_Descending() {

        String sortExpression = "sort(-employee.id)";
        DefaultSortParser sortParser = new DefaultSortParser();
        Map<String, Path> mappings = ImmutableMap.<String, Path>builder()
                .put("employee.id", QEmployee.employee.employeeNumber)
                .build();

        OrderSpecifierList orderSpecifierList = sortParser.parse(sortExpression, QuerydslSortContext.withMapping(mappings));
        assertNotNull(orderSpecifierList);

        List<OrderSpecifier> orderSpecifiers = orderSpecifierList.getOrders();
        assertNotNull(orderSpecifiers);
        assertEquals(1, orderSpecifiers.size());
        OrderSpecifier orderSpecifier = orderSpecifiers.get(0);
        assertEquals(Order.DESC, orderSpecifier.getOrder());
        assertEquals(QEmployee.employee.employeeNumber, orderSpecifier.getTarget());
    }
}
