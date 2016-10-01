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
package com.github.vineey.rql.querydsl.jpa;

import com.github.vineey.rql.RqlInput;
import com.github.vineey.rql.core.util.CollectionUtils;
import com.github.vineey.rql.core.util.StringUtils;
import com.github.vineey.rql.querydsl.AbstractQuerydslRqlParser;
import com.github.vineey.rql.querydsl.commons.select.pathtracker.SelectPathTrackerFactory;
import com.github.vineey.rql.querydsl.core.PathSet;
import com.github.vineey.rql.querydsl.core.PathSetTracker;
import com.github.vineey.rql.querydsl.filter.pathtracker.FilterPathSetTrackerFactory;
import com.github.vineey.rql.querydsl.jpa.filter.JpaQuerydslFilterContext;
import com.github.vineey.rql.querydsl.jpa.join.QuerydslJoinParam;
import com.github.vineey.rql.querydsl.jpa.join.QuerydslJpaJoinBuilder;
import com.github.vineey.rql.querydsl.jpa.select.JpaQuerydslSelectContext;
import com.github.vineey.rql.querydsl.jpa.sort.JpaQuerydslSortContext;
import com.github.vineey.rql.querydsl.page.AbstractQuerydslPageContext;
import com.github.vineey.rql.querydsl.page.QuerydslPageParam;
import com.google.common.collect.Sets;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.vineey.rql.querydsl.page.AbstractQuerydslPageContext.withDefault;

/**
 * @author vrustia - 5/29/16.
 */
public class JpaQuerydslRqlParser extends AbstractQuerydslRqlParser<JpaQuerydslMappingParam, JpaQuerydslMappingResult> {

    private final QuerydslJpaJoinBuilder jpaJoinBuilder;

    public JpaQuerydslRqlParser() {
        this.jpaJoinBuilder = new QuerydslJpaJoinBuilder();
    }

    @Override
    protected JpaQuerydslMappingResult createMappingResult(JpaQuerydslMappingParam querydslMappingParam) {
        return new JpaQuerydslMappingResult();
    }

    protected void parseSelect(RqlInput rqlInput, JpaQuerydslMappingParam querydslMappingParam, JpaQuerydslMappingResult querydslMappingResult) {
        String select = rqlInput.getSelect();

        JpaQuerydslSelectContext selectContext = JpaQuerydslSelectContext
                .withMappingAndJoinAndBuilder(querydslMappingParam.getRootPath(),
                        querydslMappingParam.getPathMapping(),
                        querydslMappingParam.getJoinMapping());

        querydslMappingResult.setProjection(selectParser.parse(select, selectContext));

        PathSetTracker selectPathSetTracker = SelectPathTrackerFactory.createTracker(select, selectContext.getSelectParam());

        querydslMappingResult.setSelectPaths(selectPathSetTracker.trackPaths().getPathSet());

    }


    protected void parseFilter(RqlInput rqlInput, Map<String, Path> pathMapping, JpaQuerydslMappingResult querydslMappingResult) {
        String filter = rqlInput.getFilter();

        if (StringUtils.isNotEmpty(filter)) {
            JpaQuerydslFilterContext filterContext = JpaQuerydslFilterContext.withMapping(pathMapping);

            buildPredicate(querydslMappingResult, filter, filterContext);

            trackFilterPaths(querydslMappingResult, filter, filterContext);
        }
    }

    protected void buildPredicate(JpaQuerydslMappingResult querydslMappingResult, String filter, JpaQuerydslFilterContext filterContext) {
        querydslMappingResult.setPredicate(filterParser.parse(filter, filterContext));
    }


    protected void trackFilterPaths(JpaQuerydslMappingResult querydslMappingResult, String filter, JpaQuerydslFilterContext filterContext) {
        PathSet filterPaths = FilterPathSetTrackerFactory.createTracker(filter, filterContext.getFilterParam()).trackPaths();
        querydslMappingResult.setFilterPaths(filterPaths.getPathSet());
    }

    protected void parseSort(RqlInput rqlInput, Map<String, Path> pathMapping, JpaQuerydslMappingResult querydslMappingResult) {
        String sort = rqlInput.getSort();
        if (StringUtils.isNotEmpty(sort)) {
            List<OrderSpecifier> orderSpecifiers = sortParser.parse(sort, JpaQuerydslSortContext.withMapping(pathMapping)).getOrders();
            querydslMappingResult.setOrderSpecifiers(orderSpecifiers);
            Set<Path> sortPathSet = Sets.newHashSet();
            for (OrderSpecifier orderSpecifier : orderSpecifiers) {
                sortPathSet.add((Path) orderSpecifier.getTarget());
            }
            querydslMappingResult.setSortPaths(sortPathSet);
        }
    }

    protected void parseLimit(RqlInput rqlInput, JpaQuerydslMappingResult querydslMappingResult) {
        String limit = rqlInput.getLimit();
        if (StringUtils.isNotEmpty(limit))
            querydslMappingResult.setPage(pageParser.parse(limit, (AbstractQuerydslPageContext<QuerydslPageParam>)withDefault()));
    }

    @Override
    public JpaQuerydslMappingResult parse(RqlInput rqlInput, JpaQuerydslMappingParam querydslMappingParam) {
        JpaQuerydslMappingResult querydslMappingResult = super.parse(rqlInput, querydslMappingParam);

        QuerydslJoinParam querydslJoinParam = buildQuerydslJoinParam(querydslMappingParam, querydslMappingResult);

        querydslMappingResult.setJoinListNode(jpaJoinBuilder.visit(querydslJoinParam));

        return querydslMappingResult;
    }

    private QuerydslJoinParam buildQuerydslJoinParam(JpaQuerydslMappingParam querydslMappingParam, JpaQuerydslMappingResult querydslMappingResult) {
        Set<Path> queryPaths = Sets.newHashSet();

        if (CollectionUtils.isNotEmpty(querydslMappingResult.getSelectPaths()))
            queryPaths.addAll(querydslMappingResult.getSelectPaths());

        if (CollectionUtils.isNotEmpty(querydslMappingResult.getFilterPaths()))
            queryPaths.addAll(querydslMappingResult.getFilterPaths());

        if (CollectionUtils.isNotEmpty(querydslMappingResult.getSortPaths()))
            queryPaths.addAll(querydslMappingResult.getSortPaths());

        return new QuerydslJoinParam().setJoinMapping(querydslMappingParam.getJoinMapping()).setPaths(queryPaths);
    }

}
