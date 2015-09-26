package com.archelix.rql.querydsl.operator;

import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;

/**
 * @author vrustia on 9/26/2015.
 */
public interface PathOperator<T extends Path> {
    BooleanExpression evaluate(T path, ComparisonNode comparisonNode);
}
