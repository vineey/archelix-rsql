/*
* MIT License
*
* Copyright (c) 2016 John Michael Vincent S. Rustia
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package com.github.vineey.rql.querydsl.filter.converter.value;

import com.github.vineey.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.github.vineey.rql.querydsl.filter.converter.ConverterConstant;
import com.github.vineey.rql.querydsl.filter.util.ConverterUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;

import java.util.List;

import static cz.jirutka.rsql.parser.ast.RSQLOperators.*;

/**
 * @author vrustia on 9/27/2015.
 */
public class NumberPathToValueConverter implements PathToValueConverter<NumberPath> {
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
            return path.gt(firstNumberArg);
        } else if (GREATER_THAN_OR_EQUAL.equals(comparisonOperator)) {
            return path.goe(firstNumberArg);
        } else if (LESS_THAN.equals(comparisonOperator)) {
            return path.lt(firstNumberArg);
        } else if (LESS_THAN_OR_EQUAL.equals(comparisonOperator)) {
            return path.loe(firstNumberArg);
        }

        throw new UnsupportedRqlOperatorException(comparisonNode, path.getClass());

    }

    private List<Number> convertToNumberArguments(NumberPath path, List<String> arguments) {
        List<Number> numberArgs = Lists.newArrayList();
        for (String arg : arguments) {
            numberArgs.add(convertToNumber(path, arg));
        }
        return numberArgs;
    }

    private Number convertToNumber(NumberPath path, String firstArg) {
        return ConverterConstant.NULL.equalsIgnoreCase(firstArg) ? null
                : ConverterUtil.convertToNumber(path.getType(), firstArg);
    }
}
