package com.github.vineey.rql.querydsl.filter.converter;

import com.mysema.query.types.Path;

/**
 * @author vrustia on 10/10/2015.
 */
public class UnsupportedFieldClassException extends RuntimeException {

    public UnsupportedFieldClassException(Class<?> fieldClass, Class<? extends Path> path) {
        super("Unsupported converter to class[" + fieldClass + "] for the given querydsl path[" + path.getSimpleName() + "]" );
    }
}
