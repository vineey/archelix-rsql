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
package com.github.vineey.rql.sort.parser.ast;

import com.github.vineey.rql.sort.parser.exception.SortParsingException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.swing.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author vrustia - 4/10/16.
 */
@RunWith(JUnit4.class)
public class SortTokenParserAdapterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseSingleSort_Ascending() {
        String sortExpression = "sort (+ employee.name) ";
        SortNodeList sortNodeList = new SortTokenParserAdapter().parse(sortExpression);
        assertNotNull(sortNodeList);
        List<SortNode> sortNodes =  sortNodeList.getNodes();
        assertEquals(1, sortNodes.size());
        SortNode sortNode = sortNodes.get(0);
        assertEquals("employee.name", sortNode.getField());
        assertEquals(SortNode.Order.ASC, sortNode.getOrder());
    }


    @Test
    public void parseSingleSort_Descending() {
        String sortExpression = "sort ( - employee.name) ";
        SortNodeList sortNodeList = new SortTokenParserAdapter().parse(sortExpression);
        assertNotNull(sortNodeList);
        List<SortNode> sortNodes =  sortNodeList.getNodes();
        assertEquals(1, sortNodes.size());
        SortNode sortNode = sortNodes.get(0);
        assertEquals("employee.name", sortNode.getField());
        assertEquals(SortNode.Order.DESC, sortNode.getOrder());
    }

    @Test
    public void parseSingleSort_Error() {
        String sortExpression = "sort (+- employee.name) ";
        thrown.expect(SortParsingException.class);
        new SortTokenParserAdapter().parse(sortExpression);
    }

    @Test
    public void parseMultipleSort() {
        String sortExpression = "sort ( + employee.name, - company.code) ";
        SortNodeList sortNodeList = new SortTokenParserAdapter().parse(sortExpression);
        assertNotNull(sortNodeList);
        List<SortNode> sortNodes =  sortNodeList.getNodes();
        assertEquals(2, sortNodes.size());
        SortNode sortNode = sortNodes.get(0);
        assertEquals("employee.name", sortNode.getField());
        assertEquals(SortNode.Order.ASC, sortNode.getOrder());
        SortNode sortNode2 = sortNodes.get(1);
        assertEquals("company.code", sortNode2.getField());
        assertEquals(SortNode.Order.DESC, sortNode2.getOrder());
    }

    @Test
    public void nonExistingSortOrder() {
        thrown.expect(IllegalArgumentException.class);
        SortNode.Order.get("*");
    }
}
