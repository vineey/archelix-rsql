package com.archelix.rql.querydsl;

import com.archelix.rql.querydsl.operator.PathOperatorContext;
import com.google.common.collect.Lists;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
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

    private BooleanExpression evaluateLogicalExpression(LogicalNode node, QuerydslFilterParam param, Operator<Boolean> logicalOperator) {

        List<Node> children = Lists.newArrayList(node.getChildren());
        Node firstNode = children.remove(0);
        BooleanExpression predicate = (BooleanExpression)firstNode.accept(this, param);
        for (Node subNode : children) {
            BooleanExpression subPredicate = (BooleanExpression)subNode.accept(this, param);
            predicate = combineByLogicalExpression(logicalOperator, predicate, subPredicate);
        }
        return predicate;
    }

    private BooleanExpression combineByLogicalExpression(Operator<Boolean> logicalOperator, BooleanExpression predicate, Predicate subPredicate) {
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
        return PathOperatorContext.getOperator(path).evaluate(path, node);
    }
}
