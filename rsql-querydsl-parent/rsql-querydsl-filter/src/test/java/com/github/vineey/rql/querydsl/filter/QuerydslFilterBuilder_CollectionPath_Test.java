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

import static com.github.vineey.rql.querydsl.filter.QEmployee.employee;
import static org.junit.Assert.assertNotNull;
import static com.github.vineey.rql.filter.FilterContext.withBuilderAndParam;

/**
 * @author vrustia - 3/25/16.
 */
@RunWith(JUnit4.class)
public class QuerydslFilterBuilder_CollectionPath_Test {

    private final static DefaultFilterParser DEFAULT_FILTER_PARSER = new DefaultFilterParser();

    @Test
    public void collectionNotEmpty() {

        Map<String, Path> pathHashMap = ImmutableMap.<String, Path>builder()
                .put("employee.nameCollection", employee.nameCollection)
                .build();

        String rqlFilter = "employee.nameCollection=sizene=0";
        Predicate predicate = DEFAULT_FILTER_PARSER.parse(rqlFilter,
                withBuilderAndParam(new QuerydslFilterBuilder(), new QuerydslFilterParam()
                        .setMapping(pathHashMap)));

        assertNotNull(predicate);
        BooleanOperation sizeExpression = (BooleanOperation) predicate;
        Assert.assertEquals("!(size(employee.nameCollection) = 0)", sizeExpression.toString());
    }

    @Test
    public void collectionEmpty() {

        Map<String, Path> pathHashMap = ImmutableMap.<String, Path>builder()
                .put("employee.nameCollection", employee.nameCollection)
                .build();

        String rqlFilter = "employee.nameCollection=size=0";
        Predicate predicate = DEFAULT_FILTER_PARSER.parse(rqlFilter,
                withBuilderAndParam(new QuerydslFilterBuilder(), new QuerydslFilterParam()
                        .setMapping(pathHashMap)));

        assertNotNull(predicate);
        BooleanOperation sizeExpression = (BooleanOperation) predicate;
        Assert.assertEquals("size(employee.nameCollection) = 0", sizeExpression.toString());
    }

    @Test
    public void collectionSizeEquals() {

        Map<String, Path> pathHashMap = ImmutableMap.<String, Path>builder()
                .put("employee.nameCollection", employee.nameCollection)
                .build();

        String rqlFilter = "employee.nameCollection=size=5";
        Predicate predicate = DEFAULT_FILTER_PARSER.parse(rqlFilter,
                withBuilderAndParam(new QuerydslFilterBuilder(), new QuerydslFilterParam()
                        .setMapping(pathHashMap)));

        assertNotNull(predicate);
        BooleanOperation sizeExpression = (BooleanOperation) predicate;
        Assert.assertEquals("size(employee.nameCollection) = 5", sizeExpression.toString());
    }
}
