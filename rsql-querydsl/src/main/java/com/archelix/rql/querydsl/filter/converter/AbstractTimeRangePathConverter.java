package com.archelix.rql.querydsl.filter.converter;

import com.archelix.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.google.common.collect.Lists;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;

import java.util.List;

import static cz.jirutka.rsql.parser.ast.RSQLOperators.*;

/**
 * @author vrustia on 10/12/2015.
 */
public abstract class AbstractTimeRangePathConverter<RANGE extends Comparable, PATH extends ComparableExpression> implements PathConverter<PATH> {
    @Override
    public BooleanExpression evaluate(PATH path, ComparisonNode comparisonNode) {
        ComparisonOperator comparisonOperator = comparisonNode.getOperator();
        List<String> arguments = comparisonNode.getArguments();
        Comparable firstTimeArg = convertArgument((Class<RANGE>) path.getType(), arguments.get(0));

        if (EQUAL.equals(comparisonOperator)) {
            return firstTimeArg == null ? path.isNull() : path.eq(firstTimeArg);
        } else if (NOT_EQUAL.equals(comparisonOperator)) {
            return firstTimeArg == null ? path.isNotNull() : path.ne(firstTimeArg);
        } else if (IN.equals(comparisonOperator)) {
            return path.in(convertToArgumentList(path, arguments));
        } else if (NOT_IN.equals(comparisonOperator)) {
            return path.notIn(convertToArgumentList(path, arguments));
        } else if (GREATER_THAN.equals(comparisonOperator)) {
            return path.gt(firstTimeArg);
        } else if (GREATER_THAN_OR_EQUAL.equals(comparisonOperator)) {
            return path.goe(firstTimeArg);
        } else if (LESS_THAN.equals(comparisonOperator)) {
            return path.lt(firstTimeArg);
        } else if (LESS_THAN_OR_EQUAL.equals(comparisonOperator)) {
            return path.loe(firstTimeArg);
        }

        throw new UnsupportedRqlOperatorException(comparisonNode, path.getClass());
    }

    protected List<RANGE> convertToArgumentList(ComparableExpression<RANGE> path, List<String> arguments) {
        Class<RANGE> pathFieldType = (Class<RANGE>) path.getType();
        List<RANGE> timeArgs = Lists.newArrayList();
        for (String arg : arguments) {
            timeArgs.add(convertArgument(pathFieldType, arg));
        }
        return timeArgs;
    }

    protected abstract RANGE convertArgument(Class<RANGE> pathFieldType, String argument);
}