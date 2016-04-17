/*
* MIT License
* 
* Copyright (c) 2016 John Michael Vincent S. Rustia
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* 
*/
package com.github.vineey.rql.querydsl.page;

import com.github.vineey.rql.page.parser.DefaultPageParser;
import com.mysema.query.QueryModifiers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.github.vineey.rql.querydsl.page.QuerydslPageContext.withDefault;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author vrustia - 4/9/16.
 */
@RunWith(JUnit4.class)
public class QuerydslPageContextTest {

    @Test
    public void parseLimit() {
        DefaultPageParser defaultPageParser = new DefaultPageParser();

        QueryModifiers querydslPage = defaultPageParser.parse("limit(10, 5)", withDefault());
        assertNotNull(querydslPage);
        assertEquals(10, querydslPage.getOffset().longValue());
        assertEquals(5, querydslPage.getLimit().longValue());
    }

    @Test
    public void parseLimit_QuerydslPageParser() {
        QuerydslPageParser defaultPageParser = new QuerydslPageParser();
        QueryModifiers querydslPage = defaultPageParser.parse("limit(10, 5)");
        assertNotNull(querydslPage);
        assertEquals(10, querydslPage.getOffset().longValue());
        assertEquals(5, querydslPage.getLimit().longValue());
    }
}
