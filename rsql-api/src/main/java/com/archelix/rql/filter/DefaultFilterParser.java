package com.archelix.rql.filter;

import cz.jirutka.rsql.parser.RSQLParser;

/**
 * @author vrustia on 9/20/2015.
 */
public class DefaultFilterParser implements FilterParser {
    private RSQLParser rsqlParser;

    public DefaultFilterParser() {
        rsqlParser = new RSQLParser();
    }

    @Override
    public <T> T parse(String rqlFilter, FilterBuilder<T> filterBuilder) {
        return filterBuilder.visit(rsqlParser.parse(rqlFilter));
    }
}
