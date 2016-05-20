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

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author vrustia - 5/7/16.
 */
public class SelectTokenParserAdapter {
    public SelectNodeList parse(String sortExpression) {
        try {
            return new SelectNodeList(createParser(sortExpression).parse());
        } catch (ParseException | Error e) {
            throw new SelectParsingException(e);
        }
    }

    private SelectTokenParser createParser(String expression) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(expression.getBytes(StandardCharsets.UTF_8));
        return new SelectTokenParser(byteArrayInputStream, StandardCharsets.UTF_8.name());

    }

}
