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
package com.github.vineey.rql.querydsl.filter.pathtracker;

import com.github.vineey.rql.querydsl.core.PathSet;
import com.github.vineey.rql.querydsl.filter.PathNotFoundException;
import com.github.vineey.rql.querydsl.filter.QuerydslFilterParam;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.querydsl.core.types.Path;
import cz.jirutka.rsql.parser.ast.*;

import java.util.List;
import java.util.Map;

/**
 * @author vrustia - 5/27/16.
 */
public class QuerydslPathSetRsqlVisitor implements RSQLVisitor<PathSet, QuerydslFilterParam> {
    private final static QuerydslPathSetRsqlVisitor RSQL_VISITOR = new QuerydslPathSetRsqlVisitor();

    public static QuerydslPathSetRsqlVisitor getInstance() {
        return RSQL_VISITOR;
    }

    @Override
    public PathSet visit(AndNode node, QuerydslFilterParam param) {
        return evaluateLogicalExpression(node, param);
    }

    @Override
    public PathSet visit(OrNode node, QuerydslFilterParam param) {
        return evaluateLogicalExpression(node, param);
    }

    private PathSet evaluateLogicalExpression(LogicalNode node, QuerydslFilterParam param) {

        List<Node> children = Lists.newArrayList(node.getChildren());
        Node firstNode = children.remove(0);
        PathSet pathSet = firstNode.accept(this, param);
        for (Node subNode : children) {
            PathSet subPathSet = subNode.accept(this, param);
            pathSet.addAll(subPathSet.getPathSet());
        }
        return pathSet;
    }

    @Override
    public PathSet visit(ComparisonNode node, QuerydslFilterParam param) {
        String selector = node.getSelector();
        Map<String, Path> mapping = param.getMapping();
        Path path = mapping.get(selector);
        if (path == null) {
            throw new PathNotFoundException("Unknown path[" + selector + "] used in the filter expression.");
        }
        return new PathSet(Sets.newHashSet(path));
    }
}
