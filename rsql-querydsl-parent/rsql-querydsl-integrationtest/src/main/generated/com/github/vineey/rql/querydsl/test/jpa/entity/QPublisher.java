package com.github.vineey.rql.querydsl.test.jpa.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.*;


/**
 * QPublisher is a Querydsl query type for Publisher
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPublisher extends EntityPathBase<Publisher> {

    private static final long serialVersionUID = -958843014L;

    public static final QPublisher publisher = new QPublisher("publisher");

    public final ListPath<Book, QBook> books = this.<Book, QBook>createList("books", Book.class, QBook.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QPublisher(String variable) {
        super(Publisher.class, forVariable(variable));
    }

    public QPublisher(Path<? extends Publisher> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPublisher(PathMetadata metadata) {
        super(Publisher.class, metadata);
    }

}

