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
package com.github.vineey.rql.page.parser.ast;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vrustia - 4/9/16.
 */
@RunWith(JUnit4.class)
public class LimitParserAdapterTest {

    private final static Logger LOG = LoggerFactory.getLogger(LimitParserAdapterTest.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseLimit() throws Exception {
        String expression = "limit(0, 10)";
        LimitParserAdapter limitParser = new LimitParserAdapter();
        PageNode pageNode = limitParser.parse(expression);
        Assert.assertNotNull(pageNode);
        LOG.debug("startIndex = {},  querySize = {})", pageNode.getStart(), pageNode.getSize());
        Assert.assertEquals(Long.valueOf(0L), pageNode.getStart());
        Assert.assertEquals(Long.valueOf(10L), pageNode.getSize());
    }

    @Test
    public void parseLimit_LexicalError_NegativeNumber() throws Exception {
        String expression = "limit(-1, 10)";
        LimitParserAdapter limitParser = new LimitParserAdapter();
        thrown.expect(LimitParsingException.class);
        limitParser.parse(expression);
    }

    @Test
    public void parseLimit_LexicalError_MispelledLimit() throws Exception {
        String expression = "limits(0, 10)";
        LimitParserAdapter limitParser = new LimitParserAdapter();
        thrown.expect(LimitParsingException.class);
        limitParser.parse(expression);
    }
}
