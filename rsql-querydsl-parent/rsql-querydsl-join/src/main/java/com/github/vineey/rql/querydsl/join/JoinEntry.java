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
package com.github.vineey.rql.querydsl.join;

import com.querydsl.core.JoinType;
import com.querydsl.core.types.EntityPath;

/**
 * @author vrustia - 5/28/16.
 */
public class JoinEntry {
    private JoinType joinType;
    private EntityPath associationPath;
    private EntityPath aliasPath;

    public EntityPath getAliasPath() {
        return aliasPath;
    }

    public JoinEntry setAliasPath(EntityPath aliasPath) {
        this.aliasPath = aliasPath;
        return this;
    }

    public EntityPath getAssociationPath() {
        return associationPath;
    }

    public JoinEntry setAssociationPath(EntityPath associationPath) {
        this.associationPath = associationPath;
        return this;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public JoinEntry setJoinType(JoinType joinType) {
        this.joinType = joinType;
        return this;
    }
}
