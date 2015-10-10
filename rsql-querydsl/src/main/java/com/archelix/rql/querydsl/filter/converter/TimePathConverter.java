package com.archelix.rql.querydsl.filter.converter;

import com.archelix.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.archelix.rql.querydsl.filter.util.LocalTimeUtil;
import com.google.common.collect.Lists;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.TimePath;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.joda.time.LocalTime;

import java.util.List;

import static com.archelix.rql.querydsl.filter.converter.ConverterConstant.NULL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.*;

/**
 * @author vrustia on 10/10/2015.
 */
public class TimePathConverter implements PathConverter<TimePath> {

    @Override
    public BooleanExpression evaluate(TimePath path, ComparisonNode comparisonNode) {
        ComparisonOperator comparisonOperator = comparisonNode.getOperator();
        List<String> arguments = comparisonNode.getArguments();
        Comparable firstTimeArg = convertToLocalTime((Class<Comparable>) path.getType(), arguments.get(0));

        if (EQUAL.equals(comparisonOperator)) {
            return firstTimeArg == null ? path.isNull() : path.eq(firstTimeArg);
        } else if (NOT_EQUAL.equals(comparisonOperator)) {
            return firstTimeArg == null ? path.isNotNull() : path.ne(firstTimeArg);
        } else if (IN.equals(comparisonOperator)) {
            return path.in(convertToTimeArguments(path, arguments));
        } else if (NOT_IN.equals(comparisonOperator)) {
            return path.notIn(convertToTimeArguments(path, arguments));
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

    private <T extends Comparable> List<T> convertToTimeArguments(TimePath<T> path, List<String> arguments) {
        Class<T> pathFieldType = (Class<T>) path.getType();
        List<T> timeArgs = Lists.newArrayList();
        for (String arg : arguments) {
            timeArgs.add(convertToLocalTime(pathFieldType, arg));
        }
        return timeArgs;
    }

    private <T> T convertToLocalTime(Class<T> pathFieldType, String argument) {
        //TODO convert arg to time with default format
        if (NULL.equalsIgnoreCase(argument)) {
            return null;
        } else {
            if (pathFieldType.equals(LocalTime.class)) return (T) LocalTimeUtil.parseLocalTime(argument);
        }

        throw new UnsupportedFieldClassException(pathFieldType, TimePath.class);
    }
}
