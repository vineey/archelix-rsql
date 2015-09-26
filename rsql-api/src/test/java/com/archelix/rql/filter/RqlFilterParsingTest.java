package com.archelix.rql.filter;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.archelix.rql.filter.pojo.Item;
import com.archelix.rql.filter.pojo.PojoQueryFilterBuilder;
import com.archelix.rql.filter.pojo.PojoQueryFilterExecutor;
import com.archelix.rql.filter.pojo.PojoQueryFilterParam;
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
        PojoQueryFilterExecutor executor = filterParser.parse(rqlFilter, FilterManager.withBuilderAndParam(new PojoQueryFilterBuilder(), new PojoQueryFilterParam()));
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

}
