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
import com.github.vineey.rql.querydsl.core.PathSetTracker;
import com.github.vineey.rql.querydsl.join.QuerydslJoinParam;
import com.github.vineey.rql.querydsl.join.QuerydslJpaJoinBuilder;
import com.github.vineey.rql.querydsl.select.QuerydslSelectContext;
import com.github.vineey.rql.querydsl.select.jpa.JpaQuerydslSelectBuilder;
import com.github.vineey.rql.querydsl.select.pathtracker.SelectPathTrackerFactory;
import com.google.common.collect.Sets;
import com.querydsl.core.types.Path;

import java.util.Set;

/**
 * @author vrustia - 5/29/16.
 */
public class JpaQuerydslRqlParser extends DefaultQuerydslRqlParser {

    private final QuerydslJpaJoinBuilder jpaJoinBuilder;

    public JpaQuerydslRqlParser() {
        this.jpaJoinBuilder = new QuerydslJpaJoinBuilder();
    }

    protected void parseSelect(RqlInput rqlInput, QuerydslMappingParam querydslMappingParam, QuerydslMappingResult querydslMappingResult) {
        String select = rqlInput.getSelect();

        QuerydslSelectContext selectContext = QuerydslSelectContext
                .withMappingAndJoinAndBuilder(querydslMappingParam.getRootPath(),
                        querydslMappingParam.getPathMapping(),
                        querydslMappingParam.getJoinMapping(),
                        new JpaQuerydslSelectBuilder());

        querydslMappingResult.setProjection(selectParser.parse(select, selectContext));

        PathSetTracker selectPathSetTracker = SelectPathTrackerFactory.createTracker(select, selectContext.getSelectParam());

        querydslMappingResult.setSelectPaths(selectPathSetTracker.trackPaths().getPathSet());

    }

    @Override
    public QuerydslMappingResult parse(RqlInput rqlInput, QuerydslMappingParam querydslMappingParam) {
        QuerydslMappingResult querydslMappingResult = super.parse(rqlInput, querydslMappingParam);

        QuerydslJoinParam querydslJoinParam = buildQuerydslJoinParam(querydslMappingParam, querydslMappingResult);

        querydslMappingResult.setJoinListNode(jpaJoinBuilder.visit(querydslJoinParam));

        return querydslMappingResult;
    }

    private QuerydslJoinParam buildQuerydslJoinParam(QuerydslMappingParam querydslMappingParam, QuerydslMappingResult querydslMappingResult) {
        Set<Path> queryPaths = Sets.newHashSet();
        queryPaths.addAll(querydslMappingResult.getSelectPaths());
        queryPaths.addAll(querydslMappingResult.getFilterPaths());
        queryPaths.addAll(querydslMappingResult.getSortPaths());
        return new QuerydslJoinParam().setJoinMapping(querydslMappingParam.getJoinMapping()).setPaths(queryPaths);
    }

}
