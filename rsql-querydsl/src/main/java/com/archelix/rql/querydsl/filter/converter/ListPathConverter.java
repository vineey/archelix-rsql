package com.archelix.rql.querydsl.filter.converter;

import com.archelix.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.ListPath;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;

import java.util.List;

import static cz.jirutka.rsql.parser.ast.RSQLOperators.IN;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.NOT_EQUAL;

/**
 * @author vrustia - 3/25/16.
 */
public class ListPathConverter implements PathConverter<ListPath> {

    @Override
    public BooleanExpression evaluate(ListPath path, ComparisonNode comparisonNode) {
        ComparisonOperator comparisonOperator = comparisonNode.getOperator();
        List<String> arguments = comparisonNode.getArguments();

        if (IN.equals(comparisonOperator)) {
            return null;
        } else if (NOT_EQUAL.equals(comparisonOperator)) {
            return emptyList(arguments) ? path.isNotEmpty() : path.contains(arguments).not();
        }

        throw new UnsupportedRqlOperatorException(comparisonNode, path.getClass());

    }

    private boolean emptyList(List<String> arguments) {
        return arguments == null || arguments.size() == 0;
    }


}
