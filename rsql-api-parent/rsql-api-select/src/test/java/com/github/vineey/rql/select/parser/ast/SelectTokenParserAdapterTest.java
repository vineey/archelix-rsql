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
package com.github.vineey.rql.select.parser.ast;

import com.github.vineey.rql.select.parser.exception.SelectParsingException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author vrustia - 5/7/16.
 */
@RunWith(JUnit4.class)
public class SelectTokenParserAdapterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseSelect() {
        String selectExpression = "select (employee.name, employee.age, employee.number) ";
        SelectNodeList sortNodeList = new SelectTokenParserAdapter().parse(selectExpression);
        assertNotNull(sortNodeList);
        List<String> sortNodes =  sortNodeList.getFields();
        assertEquals(3, sortNodes.size());
        String firstField = sortNodes.get(0);
        assertEquals("employee.name", firstField);

        String secondField = sortNodes.get(1);
        assertEquals("employee.age", secondField);

        String thirdField = sortNodes.get(2);
        assertEquals("employee.number", thirdField);

    }


    @Test
    public void parseSelect_Error() {
        String sortExpression = "select (+employee.name) ";
        thrown.expect(SelectParsingException.class);
        new SelectTokenParserAdapter().parse(sortExpression);
    }

}
