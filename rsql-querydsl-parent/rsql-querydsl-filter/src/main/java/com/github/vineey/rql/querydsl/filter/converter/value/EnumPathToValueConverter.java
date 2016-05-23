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
import com.github.vineey.rql.querydsl.filter.util.Enums;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;

import java.util.ArrayList;
import java.util.List;

import static cz.jirutka.rsql.parser.ast.RSQLOperators.*;

/**
 * @author vrustia on 9/26/2015.
 */
public class EnumPathToValueConverter implements PathToValueConverter<EnumPath> {
    @Override
    public BooleanExpression evaluate(EnumPath path, ComparisonNode comparisonNode) {
        ComparisonOperator comparisonOperator = comparisonNode.getOperator();
        List<String> arguments = comparisonNode.getArguments();

        List<Enum> enumArgs = convertArgumentsToEnum(path, arguments);

        Enum firstEnumArg = enumArgs.get(0);
        boolean firstArgEqualsNull = firstEnumArg == null;

        if (EQUAL.equals(comparisonOperator)) {
            return firstArgEqualsNull ? path.isNull() : path.eq(firstEnumArg);
        } else if (NOT_EQUAL.equals(comparisonOperator)) {
            return firstArgEqualsNull ? path.isNotNull() : path.ne(firstEnumArg);
        } else if (IN.equals(comparisonOperator)) {
            return path.in(enumArgs);
        } else if (NOT_IN.equals(comparisonOperator)) {
            return path.notIn(enumArgs);
        }
        throw new UnsupportedRqlOperatorException(comparisonNode, path.getClass());
    }

    private List<Enum> convertArgumentsToEnum(EnumPath path, List<String> arguments) {
        List<Enum> enumArgs = new ArrayList<>();
        if (arguments.size() > 0) {
            for (String argument : arguments) {
                boolean equalsNullConstant = ConverterConstant.NULL.equalsIgnoreCase(argument);
                Enum enumArg = equalsNullConstant ? null : Enums.getEnum(path.getType(), argument);
                enumArgs.add(enumArg);
            }
        }
        return enumArgs;
    }
}
