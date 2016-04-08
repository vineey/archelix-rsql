package com.github.vineey.rql.querydsl.filter.converter;

import com.github.vineey.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.BooleanPath;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.apache.commons.lang3.BooleanUtils;

import static cz.jirutka.rsql.parser.ast.RSQLOperators.EQUAL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.NOT_EQUAL;

/**
 * @author vrustia on 10/10/2015.
 */
public class BooleanPathConverter implements PathConverter<BooleanPath> {
    @Override
    public BooleanExpression evaluate(BooleanPath path, ComparisonNode comparisonNode) {
        Boolean arg = convertToBoolean(comparisonNode);
        ComparisonOperator operator = comparisonNode.getOperator();

        if (arg == null) {
            return path.isNull();
        } else {
            if (EQUAL.equals(operator)) {
                return path.eq(arg);
            } else if (NOT_EQUAL.equals(operator)) {
                return path.ne(arg).or(path.isNull());
            }
        }

        throw new UnsupportedRqlOperatorException(comparisonNode, path.getClass());
    }

    private Boolean convertToBoolean(ComparisonNode comparisonNode) {
        String firstArg = comparisonNode.getArguments().get(0);
        return ConverterConstant.NULL.equalsIgnoreCase(firstArg) ? null : BooleanUtils.toBoolean(firstArg);
    }
}
