package com.archelix.rql.querydsl.filter;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.archelix.rql.querydsl.filter.util.RSQLUtil;
import com.archelix.rql.querydsl.util.FilterAssertUtil;
import com.archelix.rql.querydsl.util.PathTestUtil;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.archelix.rql.filter.FilterManager.withBuilderAndParam;
import static com.archelix.rql.querydsl.util.FilterAssertUtil.assertFilter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author vrustia on 10/10/2015.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_BooleanPath_Test {

    private final static Logger LOG = LoggerFactory.getLogger(QuerydslFilterBuilder_BooleanPath_Test.class);

    @Test
    public void testParse_BooleanEquals() {
        String selector = "employed";
        String argument = "false";
        assertFilter(Boolean.class, selector, RSQLOperators.EQUAL, argument);
    }

    @Test
    public void testParse_BooleanNotEquals() {
        String selector = "employed";
        String argument = "true";
        String[] rqlFilters = RSQLUtil.buildAllSymbols(selector, RSQLOperators.NOT_EQUAL, argument);

        for (String rqlFilter : rqlFilters) {
            LOG.debug("RQL Expression : {}", rqlFilter);
            FilterParser filterParser = new DefaultFilterParser();
            Predicate predicate = filterParser.parse(rqlFilter, withBuilderAndParam(new QuerydslFilterBuilder(), FilterAssertUtil.withFilterParam(Boolean.class, selector)));
            assertNotNull(predicate);
            assertTrue(predicate instanceof BooleanOperation);
            BooleanOperation booleanOperation = (BooleanOperation) predicate;
            assertEquals(2, booleanOperation.getArgs().size());
            assertEquals(selector + " != " + argument, booleanOperation.getArg(0).toString());
            assertEquals(selector + " is null", booleanOperation.getArg(1).toString());
            assertEquals(Ops.OR, booleanOperation.getOperator());
        }
    }
}
