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
package com.github.vineey.rql.querydsl.mongo.select;

import com.github.vineey.rql.querydsl.test.mongo.QContactDocument;
import com.github.vineey.rql.select.parser.DefaultSelectParser;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author vrustia - 5/9/16.
 */
@RunWith(JUnit4.class)
public class MongoQuerydslSelectContextTest {


    @Test
    public void multiSelect() {
        String rqlSelectExpression = "select(contact.company, contact.name, contact.age)";
        DefaultSelectParser selectParser = new DefaultSelectParser();
        Map<String, Path> mappings = ImmutableMap.<String, Path>builder()
                .put("contact.age", QContactDocument.contactDocument.age)
                .put("contact.name", QContactDocument.contactDocument.name)
                .put("contact.bday", QContactDocument.contactDocument.bday)
                .put("contact.company", QContactDocument.contactDocument.company)
                .build();

        Expression selectExpression = selectParser.parse(rqlSelectExpression, MongoQuerydslSelectContext.withMapping(QContactDocument.contactDocument, mappings));

        assertNotNull(selectExpression);

        assertEquals(Projections.bean(QContactDocument.contactDocument,
                QContactDocument.contactDocument.company,
                QContactDocument.contactDocument.name,
                QContactDocument.contactDocument.age), selectExpression);
    }

    @Test
    public void defaultSelect() {
        String rqlSelect = "";
        DefaultSelectParser selectParser = new DefaultSelectParser();
        Map<String, Path> mappings = ImmutableMap.<String, Path>builder()
                .put("contact.age", QContactDocument.contactDocument.age)
                .put("contact.name", QContactDocument.contactDocument.name)
                .put("contact.bday", QContactDocument.contactDocument.bday)
                .put("contact.company", QContactDocument.contactDocument.company)
                .build();

        Expression selectExpression = selectParser.parse(rqlSelect, MongoQuerydslSelectContext.withMappingAndJoinAndBuilder(QContactDocument.contactDocument, mappings, null));

        assertNotNull(selectExpression);

        assertEquals(Projections.bean(QContactDocument.contactDocument,
                QContactDocument.contactDocument.age,
                QContactDocument.contactDocument.name,
                QContactDocument.contactDocument.bday,
                QContactDocument.contactDocument.company), selectExpression);
    }

}
