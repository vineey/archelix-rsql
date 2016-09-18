package com.github.vineey.rql.querydsl.jpa.page;

import com.github.vineey.rql.page.parser.DefaultPageParser;
import com.querydsl.core.QueryModifiers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class JpaQuerydslPageContextTest {

    @Test
    public void parseLimit() {
        DefaultPageParser defaultPageParser = new DefaultPageParser();

        QueryModifiers querydslPage = defaultPageParser.parse("limit(10, 5)", JpaQuerydslPageContext.withDefault());
        assertNotNull(querydslPage);
        assertEquals(10, querydslPage.getOffset().longValue());
        assertEquals(5, querydslPage.getLimit().longValue());
    }
}