/* * MIT License
 *  * Copyright (c) 2016 John Michael Vincent S. Rustia
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
 *  * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
*  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE. *  */
 package com.github.vineey.rql.querydsl.filter.converter.value;

import com.github.vineey.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.TemporalExpression;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;

import java.util.List;

import static cz.jirutka.rsql.parser.ast.RSQLOperators.*;

/**
 * @author vrustia on 10/12/2015.
 */
public abstract class AbstractTimeRangePathToValueConverter<RANGE extends Comparable, PATH extends TemporalExpression> implements PathToValueConverter<PATH> {
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

    protected List<RANGE> convertToArgumentList(TemporalExpression<RANGE> path, List<String> arguments) {
        Class<RANGE> pathFieldType = (Class<RANGE>) path.getType();
        List<RANGE> timeArgs = Lists.newArrayList();
        for (String arg : arguments) {
            timeArgs.add(convertArgument(pathFieldType, arg));
        }
        return timeArgs;
    }

    protected abstract RANGE convertArgument(Class<RANGE> pathFieldType, String argument);
}