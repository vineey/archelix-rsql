package com.github.vineey.rql.querydsl.filter;

import com.github.vineey.rql.filter.parser.DefaultFilterParser;
import com.google.common.collect.ImmutableMap;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Map;

import static com.github.vineey.rql.filter.FilterContext.withBuilderAndParam;
import static org.junit.Assert.assertNotNull;

/**
 * @author vrustia - 3/25/16.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_ListPath_Test {

    private final static DefaultFilterParser DEFAULT_FILTER_PARSER = new DefaultFilterParser();

    @Test
    public void listNotEmpty() {

        Map<String, Path> pathHashMap = ImmutableMap.<String, Path>builder()
                .put("employee.names", QEmployee.employee.names)
                .build();

        String rqlFilter = "employee.names=sizene=0";
        Predicate predicate = DEFAULT_FILTER_PARSER.parse(rqlFilter,
                withBuilderAndParam(new QuerydslFilterBuilder(), new QuerydslFilterParam()
                        .setMapping(pathHashMap)));

        assertNotNull(predicate);
        BooleanOperation sizeExpression = (BooleanOperation) predicate;
        Assert.assertEquals("!(size(employee.names) = 0)", sizeExpression.toString());
    }

    @Test
    public void listEmpty() {

        Map<String, Path> pathHashMap = ImmutableMap.<String, Path>builder()
                .put("employee.names", QEmployee.employee.names)
                .build();

        String rqlFilter = "employee.names=size=0";
        Predicate predicate = DEFAULT_FILTER_PARSER.parse(rqlFilter,
                withBuilderAndParam(new QuerydslFilterBuilder(), new QuerydslFilterParam()
                        .setMapping(pathHashMap)));

        assertNotNull(predicate);
        BooleanOperation sizeExpression = (BooleanOperation) predicate;
        Assert.assertEquals("size(employee.names) = 0", sizeExpression.toString());
    }

    @Test
    public void listSizeEquals() {

        Map<String, Path> pathHashMap = ImmutableMap.<String, Path>builder()
                .put("employee.names", QEmployee.employee.names)
                .build();

        String rqlFilter = "employee.names=size=5";
        Predicate predicate = DEFAULT_FILTER_PARSER.parse(rqlFilter,
                withBuilderAndParam(new QuerydslFilterBuilder(), new QuerydslFilterParam()
                        .setMapping(pathHashMap)));

        assertNotNull(predicate);
        BooleanOperation sizeExpression = (BooleanOperation) predicate;
        Assert.assertEquals("size(employee.names) = 5", sizeExpression.toString());
    }
}
