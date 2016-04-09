/* * MIT License
 *  * Copyright (c) 2016 John Michael Vincent S. Rustia
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
 *  * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
*  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE. *  */
 package com.github.vineey.rql.querydsl.filter;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QEmployee is a Querydsl query type for Employee
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEmployee extends EntityPathBase<Employee> {

    private static final PathInits INITS = new PathInits("*", "names.*");
    public static final QEmployee employee = new QEmployee("employee");
    private static final long serialVersionUID = 271543265L;

    public final StringPath employeeNumber = createString("employeeNumber");

    public final ListPath<Name, QName> names = this.<Name, QName>createList("names", Name.class, QName.class, INITS.get("names"));
    public final SetPath<Name, QName> nameSet = this.<Name, QName>createSet("nameSet", Name.class, QName.class, INITS.get("nameSet"));
    public final CollectionPath<Name, QName> nameCollection = this.<Name, QName>createCollection("nameCollection", Name.class, QName.class, INITS.get("nameCollection"));

    public QEmployee(String variable) {
        this(PathMetadataFactory.forVariable(variable));
    }

    public QEmployee(Path<? extends Employee> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmployee(PathMetadata<?> metadata) {
        super(Employee.class, metadata);
    }

}

