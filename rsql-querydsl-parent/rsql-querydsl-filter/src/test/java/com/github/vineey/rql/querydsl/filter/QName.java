package com.github.vineey.rql.querydsl.filter;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QName is a Querydsl query type for Name
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QName extends EntityPathBase<Name> {

    private static final long serialVersionUID = 669028670L;

    public static final QName name = new QName("name");

    public final NumberPath<Long> age = createNumber("age", Long.class);

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final StringPath firstname = createString("firstname");

    public QName(String variable) {
        super(Name.class, PathMetadataFactory.forVariable(variable));
    }

    public QName(Path<? extends Name> path) {
        super(path.getType(), path.getMetadata());
    }

    public QName(PathMetadata<?> metadata) {
        super(Name.class, metadata);
    }

}

