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
 package com.github.vineey.rql.querydsl.filter.util;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

/**
 * @author vrustia on 9/26/2015.
 */
public final class RSQLUtil {
    private final static ImmutableList<ComparisonOperator> singleArgOps = ImmutableList.<ComparisonOperator>builder()
            .add(RSQLOperators.EQUAL,
                    RSQLOperators.NOT_EQUAL,
                    RSQLOperators.GREATER_THAN,
                    RSQLOperators.GREATER_THAN_OR_EQUAL,
                    RSQLOperators.LESS_THAN,
                    RSQLOperators.LESS_THAN_OR_EQUAL)
            .build();

    public static String build(String selector, ComparisonOperator op, String... args) {
        validateArgument(args);
        if (singleArgOps.contains(op)) {
            return selector + op.getSymbol() + args[0];
        } else {
            return selector + op.getSymbol() + buildArguments(args);
        }
    }

    public static String[] buildAllSymbols(String selector, ComparisonOperator op, String... args) {
        validateArgument(args);
        String[] rqlFilters = new String[op.getSymbols().length];

        int index = 0;
        for (String symbol : op.getSymbols()) {
            if (singleArgOps.contains(op)) {
                rqlFilters[index] = selector + symbol + args[0];
            } else {
                rqlFilters[index] = selector + symbol + buildArguments(args);
            }
            index++;
        }

        return rqlFilters;
    }

    private static void validateArgument(String[] args) {
        Preconditions.checkNotNull(args);
        Preconditions.checkArgument(args.length > 0);
        for (String arg : args) {
            Preconditions.checkArgument(StringUtils.isNotEmpty(arg), "Argument should not be empty!");
        }
    }

    public static String buildArguments(String... args) {
        return "(" + Joiner.on(",").join(args) + ")";
    }
}
