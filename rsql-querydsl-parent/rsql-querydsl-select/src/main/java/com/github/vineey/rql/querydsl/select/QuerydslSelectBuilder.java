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
package com.github.vineey.rql.querydsl.select;

import com.github.vineey.rql.select.SelectBuilder;
import com.github.vineey.rql.select.parser.ast.SelectNodeList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.querydsl.core.JoinType;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.vineey.rql.core.util.AnnotationUtils.containsAnnotation;

/**
 * @author vrustia - 4/17/16.
 */
public class QuerydslSelectBuilder implements SelectBuilder<Expression, QuerydslSelectParam> {
    @Override
    public Expression visit(SelectNodeList node, QuerydslSelectParam selectParam) {

        List<Path> selectPath = mapPaths(node, selectParam);

        EntityPath rootPath = selectParam.getRootPath();

        Expression[] selectExpressionFields = selectPath.toArray(new Expression[]{});

        return Projections.bean(rootPath, selectExpressionFields);

    }

    protected List<Path> mapPaths(SelectNodeList node, QuerydslSelectParam selectParam) {
        Map<String, Path> mapping = selectParam.getMapping();

        List<Path> selectPath = new ArrayList<>();

        List<String> selectNodes = node.getFields();
        if(selectNodes != null && !selectNodes.isEmpty()) {
            for (String selectNode : selectNodes) {
                Path path = mapping.get(selectNode);
                selectPath.add(path);
            }
        }

        if(selectPath.isEmpty()) {
            selectPath.addAll(mapping.values());
        }
        return selectPath;
    }
}
