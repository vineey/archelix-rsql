package com.github.vineey.rql.querydsl.test.mongo.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.*;


/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QAddress extends BeanPath<Address> {

    private static final long serialVersionUID = -194483911L;

    public static final QAddress address1 = new QAddress("address1");

    public final StringPath address = createString("address");

    public final StringPath city = createString("city");

    public final StringPath country = createString("country");

    public final StringPath province = createString("province");

    public QAddress(String variable) {
        super(Address.class, forVariable(variable));
    }

    public QAddress(Path<? extends Address> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAddress(PathMetadata metadata) {
        super(Address.class, metadata);
    }

}

