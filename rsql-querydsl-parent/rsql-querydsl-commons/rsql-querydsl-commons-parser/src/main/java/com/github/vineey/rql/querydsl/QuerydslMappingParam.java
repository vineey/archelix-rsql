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


import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;

import java.util.Map;

/**
 * @author vrustia - 4/24/16.
 */
public class QuerydslMappingParam<T extends QuerydslMappingParam> {
    private EntityPath rootPath;
    private Map<String, Path> pathMapping;
    private Map<EntityPath, EntityPath> joinMapping;

    public EntityPath getRootPath() {
        return rootPath;
    }

    public T setRootPath(EntityPath rootPath) {
        this.rootPath = rootPath;
        return (T)this;
    }

    public Map<String, Path> getPathMapping() {
        return pathMapping;
    }

    public T setPathMapping(Map<String, Path> pathMapping) {
        this.pathMapping = pathMapping;
        return (T)this;
    }

    public Map<EntityPath, EntityPath> getJoinMapping() {
        return joinMapping;
    }

    public T setJoinMapping(Map<EntityPath, EntityPath> joinMapping) {
        this.joinMapping = joinMapping;
        return (T)this;
    }
}
