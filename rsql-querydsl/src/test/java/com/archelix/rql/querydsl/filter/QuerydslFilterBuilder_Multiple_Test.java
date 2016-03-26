package com.archelix.rql.querydsl.filter;

import com.archelix.rql.filter.parser.DefaultFilterParser;
import com.archelix.rql.filter.parser.FilterParser;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.PredicateOperation;
import com.mysema.query.types.expr.BooleanOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import static com.archelix.rql.filter.FilterContext.withBuilderAndParam;
import static com.archelix.rql.querydsl.util.FilterAssertUtil.withFilterParam;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author vrustia - 3/25/16.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_Multiple_Test {

    private final static FilterParser FILTER_PARSER = new DefaultFilterParser();

    @Test
    public void multipleFilters() {

        String rqlFilter = "(name=='Khiel' and birthDate > '2014-05-11') or (name=='Vhia' and birthDate > '2011-09-14')";

        Predicate predicate = FILTER_PARSER.parse(rqlFilter,
                withBuilderAndParam(new QuerydslFilterBuilder(),
                        withFilterParam(new LinkedHashMap.SimpleEntry<>("name", String.class),
                                new LinkedHashMap.SimpleEntry<>("birthDate", LocalDate.class))));

        assertNotNull(predicate);
        assertTrue(predicate instanceof BooleanOperation);
        BooleanOperation booleanOperation = (BooleanOperation) predicate;

        List<Expression<?>> outerArguments = booleanOperation.getArgs();
        assertEquals(2, outerArguments.size());
        assertEquals(Ops.OR, booleanOperation.getOperator());

        Expression<?> leftSideExpression = outerArguments.get(0);
        assertNotNull(leftSideExpression instanceof PredicateOperation);
        Predicate khielExpression = (PredicateOperation) leftSideExpression;
        assertEquals("eqIc(name,Khiel) && birthDate > 2014-05-11", khielExpression.toString());

        Expression<?> rightSideExpression = outerArguments.get(1);
        assertNotNull(rightSideExpression instanceof PredicateOperation);
        Predicate vhiaExpression = (PredicateOperation) rightSideExpression;
        assertEquals("eqIc(name,Vhia) && birthDate > 2011-09-14", vhiaExpression.toString());
    }

}
