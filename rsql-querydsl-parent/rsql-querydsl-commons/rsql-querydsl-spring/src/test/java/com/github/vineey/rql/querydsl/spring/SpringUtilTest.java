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
package com.github.vineey.rql.querydsl.spring;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
/**
 * FIXME
 * @author vrustia - 4/24/16.
 */
@RunWith(JUnit4.class)
@Ignore
public class SpringUtilTest {
/*
    private final static QuerydslRqlParser<QuerydslMappingParam> querydslRqlParser = new AbstractQuerydslRqlParser();

    @BeforeClass
    public static void init() {
        new SpringUtil();
    }

    @Test
    public void convertToPageable() {
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

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setRootPath(QEmployee.employee).setPathMapping(pathMapping));

        Pageable pageable = SpringUtil.toPageable(querydslMappingResult.getOrderSpecifiers(), querydslMappingResult.getPage());
        Assert.assertEquals(0, pageable.getOffset());
        Assert.assertEquals(10, pageable.getPageSize());
        Sort pageableSort = pageable.getSort();
        Assert.assertEquals(Sort.Direction.ASC, pageableSort.getOrderFor("employeeNumber").getDirection());
    }*/
}
