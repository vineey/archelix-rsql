package com.archelix.rql.filter.pojo;


/**
 * @author vrustia on 9/20/2015.
 */
public class Predicate {
    private String path;
    private String value;

    public String getPath() {
        return path;
    }

    public Predicate setPath(String path) {
        this.path = path;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Predicate setValue(String value) {
        this.value = value;
        return this;
    }
}
