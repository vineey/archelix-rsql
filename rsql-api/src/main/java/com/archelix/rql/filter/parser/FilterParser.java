package com.archelix.rql.filter.parser;

import com.archelix.rql.filter.FilterManager;
import com.archelix.rql.filter.FilterParam;

/**
 * @author vrustia on 9/19/2015.
 */
public interface FilterParser {
    <T, E extends FilterParam> T parse(String rqlFilter, FilterManager<T, E> filterManager);
}
