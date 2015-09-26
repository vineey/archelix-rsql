package com.archelix.rql.querydsl.filter.util;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

/**
 * @author vrustia on 9/26/2015.
 */
public final class RSQLUtil {
    private RSQLUtil() {
    }

    private final static ImmutableList<ComparisonOperator> singleArgOps = ImmutableList.<ComparisonOperator>builder()
            .add(RSQLOperators.EQUAL,
                    RSQLOperators.NOT_EQUAL,
                    RSQLOperators.GREATER_THAN,
                    RSQLOperators.GREATER_THAN_OR_EQUAL,
                    RSQLOperators.LESS_THAN,
                    RSQLOperators.LESS_THAN_OR_EQUAL)
            .build();

    public static String build(String selector, ComparisonOperator op, String... args) {
        assert(args != null && args.length > 0);
        if(singleArgOps.contains(op)) {
            return selector + op.getSymbol() + args[0];
        } else {
            return selector + op.getSymbol() + buildArguments(args);
        }
    }

    public static String buildArguments(String ... args) {
        return "(" + Joiner.on(",").join(args) + ")";
    }
}
