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
package com.github.vineey.rql.sort;

/**
 * @author vrustia - 4/10/16.
 */
public class SortContext<T, E extends SortParam> {
    private SortBuilder<T, E> sortBuilder;
    private E sortParam;

    public static <T, E extends SortParam> SortContext<T, E> withBuilderAndParam(SortBuilder<T, E> builder, E sortParam) {
        return new SortContext<T, E>()
                .setSortBuilder(builder)
                .setSortParam(sortParam);
    }

    public SortBuilder<T, E> getSortBuilder() {
        return sortBuilder;
    }

    public SortContext setSortBuilder(SortBuilder<T, E> sortBuilder) {
        this.sortBuilder = sortBuilder;
        return this;
    }

    public E getSortParam() {
        return sortParam;
    }

    public SortContext setSortParam(E sortParam) {
        this.sortParam = sortParam;
        return this;
    }
}
