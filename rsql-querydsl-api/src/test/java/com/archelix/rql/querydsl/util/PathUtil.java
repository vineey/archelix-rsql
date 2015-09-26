package com.archelix.rql.querydsl.util;

import com.google.common.base.Joiner;

/**
 * @author vrustia on 9/26/2015.
 */
public final class PathUtil {
    private PathUtil(){}

    public static String pathArg(String... args) {
        return "[" + Joiner.on(", ").join(args) + "]";
    }
}
