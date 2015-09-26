package com.archelix.rql.querydsl;

import com.archelix.rql.querydsl.operator.PathOperatorContext;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import cz.jirutka.rsql.parser.ast.*;

import java.util.Map;

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
        return null;
    }

    @Override
    public Predicate visit(OrNode node, QuerydslFilterParam param) {
        return null;
    }

    @Override
    public Predicate visit(ComparisonNode node, QuerydslFilterParam param) {
        String selector = node.getSelector();
        Path path = param.getMapping().get(selector);
        return PathOperatorContext.getOperator(path).evaluate(path, node);
    }
}
