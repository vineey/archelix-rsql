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
import com.github.vineey.rql.core.util.StringUtils;
import com.github.vineey.rql.filter.parser.DefaultFilterParser;
import com.github.vineey.rql.filter.parser.FilterParser;
import com.github.vineey.rql.page.parser.PageParser;
import com.github.vineey.rql.querydsl.core.PathSet;
import com.github.vineey.rql.querydsl.core.PathSetTracker;
import com.github.vineey.rql.querydsl.filter.QueryDslFilterContext;
import com.github.vineey.rql.querydsl.filter.pathtracker.FilterPathSetTrackerFactory;
import com.github.vineey.rql.querydsl.join.QuerydslJpaJoinBuilder;
import com.github.vineey.rql.querydsl.page.QuerydslPageParser;
import com.github.vineey.rql.querydsl.select.QuerydslSelectContext;
import com.github.vineey.rql.querydsl.select.pathtracker.SelectPathTrackerFactory;
import com.github.vineey.rql.querydsl.sort.QuerydslSortContext;
import com.github.vineey.rql.select.parser.DefaultSelectParser;
import com.github.vineey.rql.select.parser.SelectParser;
import com.github.vineey.rql.sort.parser.DefaultSortParser;
import com.github.vineey.rql.sort.parser.SortParser;
import com.google.common.collect.Sets;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.vineey.rql.querydsl.page.QuerydslPageContext.withDefault;

/**
 * @author vrustia - 4/24/16.
 */
public class DefaultQuerydslRqlParser implements QuerydslRqlParser {
    protected final FilterParser filterParser;
    protected final SortParser sortParser;
    protected final PageParser pageParser;
    protected final SelectParser selectParser;

    public DefaultQuerydslRqlParser() {
        this(new DefaultSelectParser(), new DefaultFilterParser(), new DefaultSortParser(), new QuerydslPageParser());
    }
    public DefaultQuerydslRqlParser(SelectParser selectParser, FilterParser filterParser, SortParser sortParser, PageParser pageParser) {

        this.filterParser = filterParser;
        this.sortParser = sortParser;
        this.pageParser = pageParser;
        this.selectParser = selectParser;
    }

    @Override
    public QuerydslMappingResult parse(RqlInput rqlInput, QuerydslMappingParam querydslMappingParam) {
        Map<String, Path> pathMapping = querydslMappingParam.getPathMapping();

        QuerydslMappingResult querydslMappingResult = new QuerydslMappingResult();

        parseSelect(rqlInput, querydslMappingParam, querydslMappingResult);

        parseFilter(rqlInput, pathMapping, querydslMappingResult);

        parseSort(rqlInput, pathMapping, querydslMappingResult);

        parseLimit(rqlInput, querydslMappingResult);

        return querydslMappingResult;
    }

    protected void parseLimit(RqlInput rqlInput, QuerydslMappingResult querydslMappingResult) {
        String limit = rqlInput.getLimit();
        if (StringUtils.isNotEmpty(limit))
            querydslMappingResult.setPage(pageParser.parse(limit, withDefault()));
    }

    protected void parseSort(RqlInput rqlInput, Map<String, Path> pathMapping, QuerydslMappingResult querydslMappingResult) {
        String sort = rqlInput.getSort();
        if (StringUtils.isNotEmpty(sort)) {
            List<OrderSpecifier> orderSpecifiers = sortParser.parse(sort, QuerydslSortContext.withMapping(pathMapping)).getOrders();
            querydslMappingResult.setOrderSpecifiers(orderSpecifiers);
            Set<Path> sortPathSet = Sets.newHashSet();
            for(OrderSpecifier orderSpecifier : orderSpecifiers) {
                sortPathSet.add((Path)orderSpecifier.getTarget());
            }
            querydslMappingResult.setSortPaths(sortPathSet);
        }
    }

    protected void parseSelect(RqlInput rqlInput, QuerydslMappingParam querydslMappingParam, QuerydslMappingResult querydslMappingResult) {
        String select = rqlInput.getSelect();

        QuerydslSelectContext selectContext = QuerydslSelectContext.withMapping(querydslMappingParam.getRootPath(), querydslMappingParam.getPathMapping());
        querydslMappingResult.setProjection(selectParser.parse(select, selectContext));
        PathSetTracker selectPathSetTracker = SelectPathTrackerFactory.createTracker(select, selectContext.getSelectParam());
        querydslMappingResult.setSelectPaths(selectPathSetTracker.trackPaths().getPathSet());
    }

    protected void parseFilter(RqlInput rqlInput, Map<String, Path> pathMapping, QuerydslMappingResult querydslMappingResult) {
        String filter = rqlInput.getFilter();

        if (StringUtils.isNotEmpty(filter)) {
            QueryDslFilterContext filterContext = QueryDslFilterContext.withMapping(pathMapping);

            buildPredicate(querydslMappingResult, filter, filterContext);

            trackFilterPaths(querydslMappingResult, filter, filterContext);
        }
    }

    protected void buildPredicate(QuerydslMappingResult querydslMappingResult, String filter, QueryDslFilterContext filterContext) {
        querydslMappingResult.setPredicate(filterParser.parse(filter, filterContext));
    }

    protected void trackFilterPaths(QuerydslMappingResult querydslMappingResult, String filter, QueryDslFilterContext filterContext) {
        PathSet filterPaths = FilterPathSetTrackerFactory.createTracker(filter, filterContext.getFilterParam()).trackPaths();
        querydslMappingResult.setFilterPaths(filterPaths.getPathSet());
    }

}
