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
package com.github.vineey.rql.querydsl.select.jpa;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vrustia - 5/29/16.
 */
public class ProjectionEntry {
    private boolean rootPath;
    private EntityPath ownerPath;
    private EntityPath aliasPath;
    private List<Path> fieldPaths = new ArrayList<>();
    private List<ProjectionEntry> projectionEntries = new ArrayList<>();

    public boolean isRootPath() {
        return rootPath;
    }

    public ProjectionEntry setRootPath(boolean rootPath) {
        this.rootPath = rootPath;
        return this;
    }

    public List<Path> getFieldPaths() {
        return fieldPaths;
    }

    public EntityPath getOwnerPath() {
        return ownerPath;
    }

    public ProjectionEntry setOwnerPath(EntityPath ownerPath) {
        this.ownerPath = ownerPath;
        return this;
    }

    public List<ProjectionEntry> getProjectionEntries() {
        return projectionEntries;
    }

    public EntityPath getAliasPath() {
        return aliasPath;
    }

    public ProjectionEntry setAliasPath(EntityPath aliasPath) {
        this.aliasPath = aliasPath;
        return this;
    }
}
