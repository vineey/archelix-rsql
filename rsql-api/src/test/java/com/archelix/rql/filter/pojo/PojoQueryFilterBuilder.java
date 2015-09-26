package com.archelix.rql.filter.pojo;

import com.archelix.rql.filter.FilterBuilder;
import com.archelix.rql.filter.pojo.PojoQueryFilterExecutor;
import com.archelix.rql.filter.pojo.PojoQueryRsqlVisitor;
import com.archelix.rql.filter.pojo.Predicate;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vrustia on 9/26/2015.
 */
public class PojoQueryFilterBuilder implements FilterBuilder<PojoQueryFilterExecutor, PojoQueryFilterParam> {
    private PojoQueryRsqlVisitor pojoQueryRsqlVisitor = new PojoQueryRsqlVisitor();

    @Override
    public PojoQueryFilterExecutor visit(Node node, PojoQueryFilterParam pojoQueryFilterParam) {
        List<Predicate> predicates = new ArrayList<>();
        if (node instanceof ComparisonNode) {
            predicates.add(pojoQueryRsqlVisitor.visit((ComparisonNode) node, null));
        }
        return new PojoQueryFilterExecutor(predicates);
    }
}
