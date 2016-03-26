package com.archelix.rql.filter.parser;

import com.archelix.rql.filter.FilterContext;
import com.archelix.rql.filter.FilterParam;

/**
 * This is an abstraction of rql filter parsing.
 * @author vrustia on 9/19/2015.
 */
public interface FilterParser {
    <T, E extends FilterParam> T parse(String rqlFilter, FilterContext<T, E> filterContext);
}
