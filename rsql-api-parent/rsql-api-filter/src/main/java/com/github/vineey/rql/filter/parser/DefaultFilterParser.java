package com.github.vineey.rql.filter.parser;

import com.github.vineey.rql.filter.FilterContext;
import com.github.vineey.rql.filter.FilterParam;
import com.github.vineey.rql.filter.operator.QRSQLOperators;
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
    public <T, E extends FilterParam> T parse(String rqlFilter, FilterContext<T, E> filterContext) {
        E filterParam = filterContext.getFilterParam();
        return filterContext.getFilterBuilder().visit(rsqlParser.parse(rqlFilter), filterParam);
    }
}
