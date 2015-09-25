package com.archelix.rql.filter.pojo;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.apache.commons.collections.CollectionUtils;

import java.util.Map;

/**
 * @author vrustia on 9/20/2015.
 */
public class PojoQueryRsqlVisitor implements RSQLVisitor<Predicate, Map<String, Object>> {
    @Override
    public Predicate visit(AndNode node, Map<String, Object> param) {
        return null;
    }

    @Override
    public Predicate visit(OrNode node, Map<String, Object> param) {
        return null;
    }

    @Override
    public Predicate visit(ComparisonNode node, Map<String, Object> param) {
        return new Predicate()
                .setPath(node.getSelector())
                .setValue(CollectionUtils.isNotEmpty(node.getArguments()) ? node.getArguments().get(0) : null);
    }
}
