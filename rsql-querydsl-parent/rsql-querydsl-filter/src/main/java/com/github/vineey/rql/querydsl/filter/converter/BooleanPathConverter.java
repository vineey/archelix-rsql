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
package com.github.vineey.rql.querydsl.filter.converter;

import com.github.vineey.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;

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
        return ConverterConstant.NULL.equalsIgnoreCase(firstArg) ? null : Boolean.valueOf(firstArg);
    }
}
