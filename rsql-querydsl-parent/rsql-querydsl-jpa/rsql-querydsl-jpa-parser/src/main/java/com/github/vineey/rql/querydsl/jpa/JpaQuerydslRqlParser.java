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
import com.github.vineey.rql.querydsl.AbstractQuerydslRqlParser;
import com.github.vineey.rql.querydsl.core.PathSetTracker;
import com.github.vineey.rql.querydsl.jpa.join.QuerydslJoinParam;
import com.github.vineey.rql.querydsl.jpa.join.QuerydslJpaJoinBuilder;
import com.github.vineey.rql.querydsl.jpa.select.JpaQuerydslSelectContext;
import com.github.vineey.rql.querydsl.commons.select.pathtracker.SelectPathTrackerFactory;
import com.google.common.collect.Sets;
import com.querydsl.core.types.Path;

import java.util.Set;

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
