package com.archelix.rql.filter;

import com.archelix.rql.filter.pojo.PojoQueryFilterExecutor;
import com.archelix.rql.filter.pojo.PojoQueryRsqlVisitor;
import com.archelix.rql.filter.pojo.Predicate;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
/**
 * @author vrustia on 9/20/2015.
 */
@RunWith(JUnit4.class)
public class RqlFilterParsingTest {

    @Test
    public void testRqlFilterParsing() {
        String rqlFilter = "name==Khiel";
        FilterParser filterParser = new DefaultFilterParser();
        PojoQueryFilterExecutor executor = filterParser.parse(rqlFilter, new PojoQueryFilterBuilder());
        List<Item> items = createItems();
        List filteredItems = executor.execute(items);

        assertNotNull(filteredItems);
        assertEquals(1, filteredItems.size());
        assertEquals(items.get(0), filteredItems.get(0));
    }

    private List<Item> createItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item()
        .setName("Khiel"));
        return items;
    }

    static class PojoQueryFilterBuilder implements FilterBuilder<PojoQueryFilterExecutor> {
        private PojoQueryRsqlVisitor pojoQueryRsqlVisitor = new PojoQueryRsqlVisitor();
        @Override
        public PojoQueryFilterExecutor visit(Node node) {
            List<Predicate> predicates = new ArrayList<>();
            if (node instanceof ComparisonNode) {
                predicates.add(pojoQueryRsqlVisitor.visit((ComparisonNode)node, null));
            }
            return new PojoQueryFilterExecutor(predicates);
        }
    }

}
