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
package com.github.vineey.rql.querydsl.sort;

import com.github.vineey.rql.sort.SortVisitor;
import com.github.vineey.rql.sort.parser.ast.SortNode;
import com.github.vineey.rql.sort.parser.ast.SortNodeList;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vrustia - 4/17/16.
 */
public abstract class AbstractQuerydslSortVisitor<PARAM extends QuerydslSortParam> implements SortVisitor<OrderSpecifierList, PARAM> {
    @Override
    public OrderSpecifierList visit(SortNodeList node, PARAM filterParam) {
        List<SortNode> sortNodes = node.getNodes();

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        Map<String, Path> mapping = filterParam.getMapping();
        for(SortNode sortNode : sortNodes){
            Order order = SortNode.Order.DESC.equals(sortNode.getOrder()) ? Order.DESC : Order.ASC;
            Path path = mapping.get(sortNode.getField());
            OrderSpecifier orderSpecifier = new OrderSpecifier(order, path);
            orderSpecifiers.add(orderSpecifier);
        }
        return new OrderSpecifierList(orderSpecifiers);
    }
}
