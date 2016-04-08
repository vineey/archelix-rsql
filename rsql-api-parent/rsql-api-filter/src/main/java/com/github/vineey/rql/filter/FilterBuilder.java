package com.github.vineey.rql.filter;

import cz.jirutka.rsql.parser.ast.Node;

/**
 * @author vrustia on 9/20/2015.
 */
public interface FilterBuilder<T, E extends FilterParam> {
    T visit(Node node, E filterParam);
}
