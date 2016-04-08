package com.github.vineey.rql.querydsl.util;

import com.mysema.query.types.Path;

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
