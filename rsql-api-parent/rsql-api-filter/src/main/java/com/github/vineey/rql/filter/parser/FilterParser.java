package com.github.vineey.rql.filter.parser;

import com.github.vineey.rql.filter.FilterContext;
import com.github.vineey.rql.filter.FilterParam;

/**
 * This is an abstraction of rql filter parsing.
 * @author vrustia on 9/19/2015.
 */
public interface FilterParser {
    <T, E extends FilterParam> T parse(String rqlFilter, FilterContext<T, E> filterContext);
}
