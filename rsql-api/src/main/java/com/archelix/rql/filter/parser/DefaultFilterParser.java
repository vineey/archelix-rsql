package com.archelix.rql.filter.parser;

import com.archelix.rql.filter.FilterManager;
import com.archelix.rql.filter.FilterParam;
import com.archelix.rql.filter.operator.QRSQLOperators;
import cz.jirutka.rsql.parser.RSQLParser;

/**
 * @author vrustia on 9/20/2015.
 */
public class DefaultFilterParser implements FilterParser {
    private RSQLParser rsqlParser;

    public DefaultFilterParser() {
        rsqlParser = new RSQLParser(QRSQLOperators.getOperators());
    }

    @Override
    public <T, E extends FilterParam> T parse(String rqlFilter, FilterManager<T, E> filterManager) {
        E filterParam = filterManager.getFilterParam();
        return filterManager.getFilterBuilder().visit(rsqlParser.parse(rqlFilter), filterParam);
    }
}
