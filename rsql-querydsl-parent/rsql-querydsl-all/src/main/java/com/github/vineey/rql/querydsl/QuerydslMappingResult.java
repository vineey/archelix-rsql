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
package com.github.vineey.rql.querydsl;

import com.mysema.query.QueryModifiers;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

import java.util.List;

/**
 * @author vrustia - 4/24/16.
 */
public class QuerydslMappingResult {
    private Predicate predicate;
    private List<OrderSpecifier> orderSpecifiers;
    private QueryModifiers page;

    public List<OrderSpecifier> getOrderSpecifiers() {
        return orderSpecifiers;
    }

    public QuerydslMappingResult setOrderSpecifiers(List<OrderSpecifier> orderSpecifiers) {
        this.orderSpecifiers = orderSpecifiers;
        return this;
    }

    public QueryModifiers getPage() {
        return page;
    }

    public QuerydslMappingResult setPage(QueryModifiers page) {
        this.page = page;
        return this;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public QuerydslMappingResult setPredicate(Predicate predicate) {
        this.predicate = predicate;
        return this;
    }
}
