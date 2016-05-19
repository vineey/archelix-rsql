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

import com.github.vineey.rql.core.util.CollectionUtils;
import com.github.vineey.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.SimpleExpression;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;

import java.util.Collection;
import java.util.List;

import static com.github.vineey.rql.filter.operator.QRSQLOperators.SIZE_EQ;
import static com.github.vineey.rql.filter.operator.QRSQLOperators.SIZE_NOT_EQ;

/**
 * @author vrustia - 3/25/16.
 */
public abstract class AbstractCollectionPathConverter<E, Q extends SimpleExpression<? super E>, COLLECTION extends Collection<E>, PATH extends CollectionPathBase<COLLECTION, E, Q>> implements PathConverter<PATH> {
    @Override
    public BooleanExpression evaluate(PATH path, ComparisonNode comparisonNode) {
        ComparisonOperator comparisonOperator = comparisonNode.getOperator();
        String argument = getArgument(comparisonNode);

        if (SIZE_EQ.equals(comparisonOperator)) {
            return path.size().eq(convertToSize(argument));
        } else if (SIZE_NOT_EQ.equals(comparisonOperator)) {
            return path.size().eq(convertToSize(argument)).not();
        }

        throw new UnsupportedRqlOperatorException(comparisonNode, path.getClass());
    }

    private String getArgument(ComparisonNode comparisonNode) {
        List<String> arguments = comparisonNode.getArguments();
        return CollectionUtils.isNotEmpty(arguments) ? arguments.get(0) : null;
    }

    private Integer convertToSize(String arg) {
        return Integer.valueOf(arg);
    }
}
