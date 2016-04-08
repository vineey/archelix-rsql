package com.github.vineey.rql.querydsl.filter.converter;

import com.github.vineey.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.google.common.collect.Lists;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.NumberPath;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.List;

import static cz.jirutka.rsql.parser.ast.RSQLOperators.*;

/**
 * @author vrustia on 9/27/2015.
 */
public class NumberPathConverter implements PathConverter<NumberPath> {
    @Override
    public BooleanExpression evaluate(NumberPath path, ComparisonNode comparisonNode) {
        ComparisonOperator comparisonOperator = comparisonNode.getOperator();
        List<String> arguments = comparisonNode.getArguments();
        Number firstNumberArg = convertToNumber(path, arguments.get(0));

        if (EQUAL.equals(comparisonOperator)) {
            return firstNumberArg == null ? path.isNull() : path.eq(firstNumberArg);
        } else if (NOT_EQUAL.equals(comparisonOperator)) {
            return firstNumberArg == null ? path.isNotNull() : path.ne(firstNumberArg);
        } else if (IN.equals(comparisonOperator)) {
            return path.in(convertToNumberArguments(path, arguments));
        } else if (NOT_IN.equals(comparisonOperator)) {
            return path.notIn(convertToNumberArguments(path, arguments));
        } else if (GREATER_THAN.equals(comparisonOperator)) {
            return  path.gt(firstNumberArg);
        } else if (GREATER_THAN_OR_EQUAL.equals(comparisonOperator)) {
            return  path.goe(firstNumberArg);
        } else if (LESS_THAN.equals(comparisonOperator)) {
            return path.lt(firstNumberArg);
        } else if (LESS_THAN_OR_EQUAL.equals(comparisonOperator)) {
            return path.loe(firstNumberArg);
        }

        throw new UnsupportedRqlOperatorException(comparisonNode, path.getClass());

    }

    private List<Number> convertToNumberArguments(NumberPath path, List<String> arguments) {
        List<Number> numberArgs = Lists.newArrayList();
        for(String arg : arguments) {
            numberArgs.add(convertToNumber(path, arg));
        }
        return numberArgs;
    }

    private Number convertToNumber(NumberPath path, String firstArg) {
        return ConverterConstant.NULL.equalsIgnoreCase(firstArg) ? null
                    : path.getType().equals(Long.class) ? NumberUtils.createLong(firstArg)
                    : path.getType().equals(Double.class) ? NumberUtils.createDouble(firstArg)
                    : path.getType().equals(Integer.class) ? NumberUtils.createInteger(firstArg)
                    : path.getType().equals(BigDecimal.class) ? NumberUtils.createBigDecimal(firstArg)
                    : NumberUtils.createNumber(firstArg);
    }
}
