package com.archelix.rql.querydsl.filter;

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
