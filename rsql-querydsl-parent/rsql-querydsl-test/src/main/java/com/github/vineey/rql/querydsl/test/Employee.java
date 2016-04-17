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
 package com.github.vineey.rql.querydsl.test;

import com.mysema.query.annotations.QueryInit;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author vrustia - 3/26/16.
 */
@Entity
public class Employee {
    private String employeeNumber;

    @QueryInit({"*"})
    private List<Name> names;

    @QueryInit({"*"})
    private Set<Name> nameSet;

    @QueryInit({"*"})
    private Collection<Name> nameCollection;

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public Set<Name> getNameSet() {
        return nameSet;
    }

    public void setNameSet(Set<Name> nameSet) {
        this.nameSet = nameSet;
    }

    public Collection<Name> getNameCollection() {
        return nameCollection;
    }

    public void setNameCollection(Collection<Name> nameCollection) {
        this.nameCollection = nameCollection;
    }
}
