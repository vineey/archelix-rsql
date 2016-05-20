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
 package com.github.vineey.rql.querydsl.filter.converter;

import com.github.vineey.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;

import java.util.List;

import static cz.jirutka.rsql.parser.ast.RSQLOperators.*;

/**
 * @author vrustia on 9/26/2015.
 */
public class StringPathConverter implements PathConverter<StringPath> {
    private static final String WILDCARD = "*";

    @Override
    public BooleanExpression evaluate(StringPath path, ComparisonNode comparisonNode) {
        ComparisonOperator comparisonOperator = comparisonNode.getOperator();
        List<String> arguments = comparisonNode.getArguments();
        String firstArg = arguments.get(0);
        if (EQUAL.equals(comparisonOperator)) {
            return ConverterConstant.NULL.equalsIgnoreCase(firstArg) ? path.isNull() : equal(path, firstArg);
        } else if (NOT_EQUAL.equals(comparisonOperator)) {
            return ConverterConstant.NULL.equalsIgnoreCase(firstArg) ? path.isNotNull() : equal(path, firstArg).not().or(path.isNull());
        } else if (IN.equals(comparisonOperator)) {
            return path.in(arguments);
        } else if (NOT_IN.equals(comparisonOperator)) {
            return path.notIn(arguments);
        }

        throw new UnsupportedRqlOperatorException(comparisonNode, path.getClass());
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
