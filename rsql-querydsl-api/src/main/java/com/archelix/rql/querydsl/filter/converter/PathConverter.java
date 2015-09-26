package com.archelix.rql.querydsl.filter.converter;

import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;
import cz.jirutka.rsql.parser.ast.ComparisonNode;

/**
 * @author vrustia on 9/26/2015.
 */
public interface PathConverter<T extends Path> {
    BooleanExpression evaluate(T path, ComparisonNode comparisonNode);
}
