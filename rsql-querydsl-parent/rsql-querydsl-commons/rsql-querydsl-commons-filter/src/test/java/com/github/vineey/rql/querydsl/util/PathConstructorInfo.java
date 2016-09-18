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
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
 package com.github.vineey.rql.querydsl.util;


import com.querydsl.core.types.Path;

/**
 * @author vrustia on 10/10/2015.
 */
public class PathConstructorInfo<K> {
    private Class<? extends Path> pathClass;
    private Class<K> fieldType;

    public static <K> PathConstructorInfo withConstructor(Class<? extends Path> pathClass, Class<K> fieldType) {
        return new PathConstructorInfo()
                .setPathClass(pathClass)
                .setFieldType(fieldType);
    }

    public static <K> PathConstructorInfo withConstructor(Class<? extends Path> pathClass) {
        return new PathConstructorInfo()
                .setPathClass(pathClass);
    }

    public Class<? extends Path> getPathClass() {
        return pathClass;
    }

    public PathConstructorInfo setPathClass(Class<? extends Path> pathClass) {
        this.pathClass = pathClass;
        return this;
    }

    public Class<K> getFieldType() {
        return fieldType;
    }

    public PathConstructorInfo setFieldType(Class<K> fieldType) {
        this.fieldType = fieldType;
        return this;
    }
}
