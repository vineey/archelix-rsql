package com.github.vineey.rql.querydsl.filter;

import com.github.vineey.rql.filter.parser.DefaultFilterParser;
import com.github.vineey.rql.filter.parser.FilterParser;
import com.github.vineey.rql.querydsl.filter.util.RSQLUtil;
import com.github.vineey.rql.querydsl.util.FilterAssertUtil;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.vineey.rql.querydsl.filter.QueryDslFilterContextUtil.withMapping;
import static org.junit.Assert.*;

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
        FilterAssertUtil.assertFilter(Boolean.class, selector, RSQLOperators.EQUAL, argument);
    }

    @Test
    public void testParse_BooleanNotEquals() {
        String selector = "employed";
        String argument = "true";
        String[] rqlFilters = RSQLUtil.buildAllSymbols(selector, RSQLOperators.NOT_EQUAL, argument);

        for (String rqlFilter : rqlFilters) {
            LOG.debug("RQL Expression : {}", rqlFilter);
            FilterParser filterParser = new DefaultFilterParser();
            Predicate predicate = filterParser.parse(rqlFilter, withMapping(FilterAssertUtil.buildPathMap(Boolean.class, selector)));
            assertNotNull(predicate);
            assertTrue(predicate instanceof BooleanOperation);
            BooleanOperation booleanOperation = (BooleanOperation) predicate;
            Assert.assertEquals(2, booleanOperation.getArgs().size());
            Assert.assertEquals(selector + " != " + argument, booleanOperation.getArg(0).toString());
            Assert.assertEquals(selector + " is null", booleanOperation.getArg(1).toString());
            Assert.assertEquals(Ops.OR, booleanOperation.getOperator());
        }
    }
}
