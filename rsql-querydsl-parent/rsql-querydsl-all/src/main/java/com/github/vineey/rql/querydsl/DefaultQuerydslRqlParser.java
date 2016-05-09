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
package com.github.vineey.rql.querydsl;

import com.github.vineey.rql.RqlInput;
import com.github.vineey.rql.filter.parser.DefaultFilterParser;
import com.github.vineey.rql.filter.parser.FilterParser;
import com.github.vineey.rql.querydsl.filter.QueryDslFilterContext;
import com.github.vineey.rql.core.util.StringUtils;
import com.github.vineey.rql.querydsl.page.QuerydslPageParser;
import com.github.vineey.rql.querydsl.select.QuerydslSelectContext;
import com.github.vineey.rql.querydsl.sort.QuerydslSortContext;
import com.github.vineey.rql.select.parser.DefaultSelectParser;
import com.github.vineey.rql.select.parser.SelectParser;
import com.github.vineey.rql.sort.parser.DefaultSortParser;
import com.github.vineey.rql.sort.parser.SortParser;
import com.mysema.query.types.Path;

import java.util.Map;

/**
 * @author vrustia - 4/24/16.
 */
public class DefaultQuerydslRqlParser implements QuerydslRqlParser {
    private final FilterParser filterParser;
    private final SortParser sortParser;
    private final QuerydslPageParser pageParser;
    private final SelectParser selectParser;

    public DefaultQuerydslRqlParser() {
        this.filterParser = new DefaultFilterParser();
        this.sortParser = new DefaultSortParser();
        this.pageParser = new QuerydslPageParser();
        this.selectParser = new DefaultSelectParser();
    }

    @Override
    public QuerydslMappingResult parse(RqlInput rqlInput, QuerydslMappingParam querydslMappingParam) {
        Map<String, Path> pathMapping = querydslMappingParam.getPathMapping();

        QuerydslMappingResult querydslMappingResult = new QuerydslMappingResult();

        String select = rqlInput.getSelect();
        querydslMappingResult.setProjection(selectParser.parse(select, QuerydslSelectContext.withMapping(querydslMappingParam.getRootPath(), querydslMappingParam.getPathMapping())));

        String filter = rqlInput.getFilter();
        if (StringUtils.isNotEmpty(filter))
            querydslMappingResult.setPredicate(filterParser.parse(filter, QueryDslFilterContext.withMapping(pathMapping)));


        String sort = rqlInput.getSort();
        if (StringUtils.isNotEmpty(sort))
            querydslMappingResult.setOrderSpecifiers(sortParser.parse(sort, QuerydslSortContext.withMapping(pathMapping)).getOrders());

        String limit = rqlInput.getLimit();
        if (StringUtils.isNotEmpty(limit))
            querydslMappingResult.setPage(pageParser.parse(limit));

        return querydslMappingResult;
    }
}
