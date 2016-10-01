package com.github.vineey.rql.querydsl.test.mongo.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.*;


/**
 * QContact is a Querydsl query type for Contact
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContact extends EntityPathBase<Contact> {

    private static final long serialVersionUID = 1904734565L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContact contact = new QContact("contact");

    public final QAddress address;

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final StringPath email = createString("email");

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QContact(String variable) {
        this(Contact.class, forVariable(variable), INITS);
    }

    public QContact(Path<? extends Contact> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContact(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContact(PathMetadata metadata, PathInits inits) {
        this(Contact.class, metadata, inits);
    }

    public QContact(Class<? extends Contact> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new QAddress(forProperty("address")) : null;
    }

}

