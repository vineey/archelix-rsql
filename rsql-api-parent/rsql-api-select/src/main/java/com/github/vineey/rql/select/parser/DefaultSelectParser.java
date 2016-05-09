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
package com.github.vineey.rql.select.parser;

import com.github.vineey.rql.core.util.StringUtils;
import com.github.vineey.rql.select.SelectContext;
import com.github.vineey.rql.select.SelectParam;
import com.github.vineey.rql.select.parser.ast.SelectNodeList;
import com.github.vineey.rql.select.parser.ast.SelectTokenParserAdapter;

import java.util.Collections;

/**
 * @author vrustia - 5/9/16.
 */
public class DefaultSelectParser implements SelectParser {
    @Override
    public <T, E extends SelectParam> T parse(String selectExpression, SelectContext<T, E> selectContext) {
        E selectParam = selectContext.getSelectParam();
        SelectNodeList selectNodeList = StringUtils.isNotEmpty(selectExpression) ? new SelectTokenParserAdapter().parse(selectExpression) : new SelectNodeList(Collections.EMPTY_LIST);
        return selectContext.getSelectBuilder().visit(selectNodeList, selectParam);

    }
}
