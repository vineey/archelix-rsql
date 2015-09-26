package com.archelix.rql.querydsl.filter.converter;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.EnumPath;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;

import java.util.List;

import static com.archelix.rql.querydsl.filter.converter.ConverterConstant.NULL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.*;
import org.apache.commons.lang3.EnumUtils;

/**
 * @author vrustia on 9/26/2015.
 */
public class EnumPathConverter implements PathConverter<EnumPath> {
    @Override
    public BooleanExpression evaluate(EnumPath path, ComparisonNode comparisonNode) {
        ComparisonOperator comparisonOperator = comparisonNode.getOperator();
        List<String> arguments = comparisonNode.getArguments();
        String firstArg = arguments.get(0);

        Enum enumArg = EnumUtils.getEnum(path.getType(), firstArg.toUpperCase());
        if (enumArg == null && !NULL.equalsIgnoreCase(firstArg)) {
            throw new IllegalArgumentException("Nonexistent enum value:" + firstArg);
        }

        if (EQUAL.equals(comparisonOperator)) {
            return NULL.equalsIgnoreCase(firstArg) ? path.isNull() : path.eq(enumArg);
        } else if (NOT_EQUAL.equals(comparisonOperator)) {
            return NULL.equalsIgnoreCase(firstArg) ? path.isNotNull() : path.ne(enumArg);
        } else if (IN.equals(comparisonOperator)) {
            return path.in(arguments);
        } else if (NOT_IN.equals(comparisonOperator)) {
            return path.notIn(arguments);
        }

        throw new UnsupportedOperationException("The comparison operator [" + comparisonOperator.toString() + "] is not supported on strings.");
    }
}
