package com.archelix.rql.querydsl.operator;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.StringPath;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

import java.util.List;

/**
 * @author vrustia on 9/26/2015.
 */
public class StringPathOperator implements PathOperator<StringPath> {
    private static final String WILDCARD = "*";
    private static final String NULL = "NULL";

    @Override
    public BooleanExpression evaluate(StringPath path, ComparisonNode comparisonNode) {
        ComparisonOperator comparisonOperator = comparisonNode.getOperator();
        List<String> arguments = comparisonNode.getArguments();
        String firstArg = arguments.get(0);
        if (RSQLOperators.EQUAL.equals(comparisonOperator)) {
            return NULL.equalsIgnoreCase(firstArg) ? path.isNull() : equal(path, firstArg);
        } else if (RSQLOperators.NOT_EQUAL.equals(comparisonOperator)) {
            return NULL.equalsIgnoreCase(firstArg) ? path.isNotNull() : equal(path, firstArg).not().or(path.isNull());
        } else if (RSQLOperators.IN.equals(comparisonOperator)) {
            return path.in(arguments);
        } else if (RSQLOperators.NOT_IN.equals(comparisonOperator)) {
            return path.notIn(arguments);
        }

        throw new UnsupportedOperationException("The comparison operator [" + comparisonOperator.toString() + "] is not supported on strings.");
    }

    private BooleanExpression equal(StringExpression stringPath, String valueArgument) {
        if (valueArgument.startsWith(WILDCARD) && valueArgument.endsWith(WILDCARD)) {
            return stringPath.containsIgnoreCase(sanitizeWildcard(valueArgument));
        } else if (valueArgument.endsWith(WILDCARD)) {
            return stringPath.startsWithIgnoreCase(sanitizeWildcard(valueArgument));
        } else if (valueArgument.startsWith(WILDCARD)) {
            return stringPath.endsWithIgnoreCase(sanitizeWildcard(valueArgument));
        } else {
            return stringPath.equalsIgnoreCase(valueArgument);
        }
    }

    private String sanitizeWildcard(String argument) {
        return argument.replace(WILDCARD, "");
    }
}
