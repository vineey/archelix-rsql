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
package com.github.vineey.rql.querydsl.select.pathtracker;

import com.github.vineey.rql.querydsl.core.PathSet;
import com.github.vineey.rql.querydsl.select.QuerydslSelectParam;
import com.github.vineey.rql.select.SelectBuilder;
import com.github.vineey.rql.select.parser.ast.SelectNodeList;
import com.querydsl.core.types.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vrustia - 5/27/16.
 */
public class QuerydslSelectPathBuilder implements SelectBuilder<PathSet, QuerydslSelectParam> {

    @Override
    public PathSet visit(SelectNodeList node, QuerydslSelectParam selectParam) {

        List<Path> selectPath = new ArrayList<>();
        Map<String, Path> mapping = selectParam.getMapping();

        List<String> selectNodes = node.getFields();
        if (selectNodes != null && !selectNodes.isEmpty()) {
            for (String selectNode : selectNodes) {
                Path path = mapping.get(selectNode);
                selectPath.add(path);
            }
        }
        return new PathSet(selectPath);

    }
}
