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
package com.github.vineey.rql.select;

/**
 * @author vrustia - 5/7/16.
 */
public class SelectContext<T, E extends SelectParam> {

    private SelectVisitor<T, E> selectVisitor;
    private E selectParam;

    public static <T, E extends SelectParam> SelectContext<T, E> withBuilderAndParam(SelectVisitor<T, E> builder, E sortParam) {
        return new SelectContext<T, E>()
                .setSelectVisitor(builder)
                .setSelectParam(sortParam);
    }

    public SelectVisitor<T, E> getSelectVisitor() {
        return selectVisitor;
    }

    public SelectContext<T, E> setSelectVisitor(SelectVisitor<T, E> selectVisitor) {
        this.selectVisitor = selectVisitor;
        return this;
    }

    public E getSelectParam() {
        return selectParam;
    }

    public SelectContext<T, E> setSelectParam(E sortParam) {
        this.selectParam = sortParam;
        return this;
    }
}
