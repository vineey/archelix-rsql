package com.archelix.rql.querydsl.util;

import com.google.common.base.Joiner;

/**
 * @author vrustia on 9/26/2015.
 */
public final class PathTestUtil {
    private PathTestUtil(){}

    public static String pathArg(String... args) {
        return "[" + Joiner.on(", ").join(args) + "]";
    }
}
