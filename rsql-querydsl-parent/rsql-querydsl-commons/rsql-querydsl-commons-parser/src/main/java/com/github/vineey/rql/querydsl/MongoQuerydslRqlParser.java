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
import com.github.vineey.rql.querydsl.core.PathSetTracker;
import com.github.vineey.rql.querydsl.select.mongo.MongoQuerydslSelectContext;
import com.github.vineey.rql.querydsl.commons.select.pathtracker.SelectPathTrackerFactory;
import com.querydsl.core.types.Path;

import java.util.Map;

/**
 * @author vrustia - 5/29/16.
 */
public class MongoQuerydslRqlParser extends AbstractQuerydslRqlParser<MongoQuerydslMappingParam, MongoQuerydslMappingResult> {

    public MongoQuerydslRqlParser() {
    }

    @Override
    protected MongoQuerydslMappingResult createMappingResult(MongoQuerydslMappingParam querydslMappingParam) {
        return new MongoQuerydslMappingResult();
    }

    protected void parseSelect(RqlInput rqlInput, MongoQuerydslMappingParam querydslMappingParam, MongoQuerydslMappingResult querydslMappingResult) {
        String select = rqlInput.getSelect();

        MongoQuerydslSelectContext selectContext = MongoQuerydslSelectContext
                .withMappingAndJoinAndBuilder(querydslMappingParam.getRootPath(),
                        querydslMappingParam.getPathMapping(),
                        querydslMappingParam.getJoinMapping());

        querydslMappingResult.setProjection(selectParser.parse(select, selectContext));

        PathSetTracker selectPathSetTracker = SelectPathTrackerFactory.createTracker(select, selectContext.getSelectParam());

        querydslMappingResult.setSelectPaths(selectPathSetTracker.trackPaths().getPathSet());

    }

    @Override
    protected void parseFilter(RqlInput rqlInput, Map<String, Path> pathMapping, MongoQuerydslMappingResult querydslMappingResult) {

    }

    @Override
    public MongoQuerydslMappingResult parse(RqlInput rqlInput, MongoQuerydslMappingParam querydslMappingParam) {

        MongoQuerydslMappingResult querydslMappingResult = super.parse(rqlInput, querydslMappingParam);

        return querydslMappingResult;
    }

}
