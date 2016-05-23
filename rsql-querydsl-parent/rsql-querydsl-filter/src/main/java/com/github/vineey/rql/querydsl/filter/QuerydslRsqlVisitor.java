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
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
 package com.github.vineey.rql.querydsl.filter;

import com.github.vineey.rql.querydsl.filter.converter.PathToValueConverterContext;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import cz.jirutka.rsql.parser.ast.*;

import java.util.List;

/**
 * @author vrustia on 9/26/2015.
 */
public class QuerydslRsqlVisitor implements RSQLVisitor<Predicate, QuerydslFilterParam> {
    private final static QuerydslRsqlVisitor QUERYDSL_RSQL_VISITOR = new QuerydslRsqlVisitor();

    public static QuerydslRsqlVisitor getInstance() {
        return QUERYDSL_RSQL_VISITOR;
    }

    @Override
    public Predicate visit(AndNode node, QuerydslFilterParam param) {
        return evaluateLogicalExpression(node, param, Ops.AND);
    }

    private BooleanExpression evaluateLogicalExpression(LogicalNode node, QuerydslFilterParam param, Ops logicalOperator) {

        List<Node> children = Lists.newArrayList(node.getChildren());
        Node firstNode = children.remove(0);
        BooleanExpression predicate = (BooleanExpression) firstNode.accept(this, param);
        for (Node subNode : children) {
            BooleanExpression subPredicate = (BooleanExpression) subNode.accept(this, param);
            predicate = combineByLogicalExpression(logicalOperator, predicate, subPredicate);
        }
        return predicate;
    }

    private BooleanExpression combineByLogicalExpression(Ops logicalOperator, BooleanExpression predicate, Predicate subPredicate) {
        BooleanExpression combinedPredicate = predicate;
        if (Ops.AND.equals(logicalOperator)) {
            combinedPredicate = predicate.and(subPredicate);
        } else if (Ops.OR.equals(logicalOperator)) {
            combinedPredicate = predicate.or(subPredicate);
        }
        return combinedPredicate;
    }

    @Override
    public Predicate visit(OrNode node, QuerydslFilterParam param) {
        return evaluateLogicalExpression(node, param, Ops.OR);
    }

    @Override
    public Predicate visit(ComparisonNode node, QuerydslFilterParam param) {
        String selector = node.getSelector();
        Path path = param.getMapping().get(selector);
        return PathToValueConverterContext.getOperator(path).evaluate(path, node);
    }
}
