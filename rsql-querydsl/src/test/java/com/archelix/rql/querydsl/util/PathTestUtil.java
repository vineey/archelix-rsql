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

    public static String quote(String argument) {
        return "'" + argument + "'";
    }

    public static String unquote(String argument) {
        int length = argument.length();
        return argument.substring(0, 1).equals("'") && argument.substring(length - 1, length).equals("'") ? argument.substring(1, length - 1) : argument;
    }
}
