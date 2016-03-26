package com.archelix.rql.querydsl.filter;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QEmployee is a Querydsl query type for Employee
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEmployee extends EntityPathBase<Employee> {

    private static final long serialVersionUID = 271543265L;

    private static final PathInits INITS = new PathInits("*", "names.*");

    public static final QEmployee employee = new QEmployee("employee");

    public final StringPath employeeNumber = createString("employeeNumber");

    public final ListPath<Name, QName> names = this.<Name, QName>createList("names", Name.class, QName.class, INITS.get("names"));
    public final SetPath<Name, QName> nameSet = this.<Name, QName>createSet("nameSet", Name.class, QName.class, INITS.get("nameSet"));
    public final CollectionPath<Name, QName> nameCollection = this.<Name, QName>createCollection("nameCollection", Name.class, QName.class, INITS.get("nameCollection"));

    public QEmployee(String variable) {
        super(Employee.class, forVariable(variable));
    }

    public QEmployee(Path<? extends Employee> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmployee(PathMetadata<?> metadata) {
        super(Employee.class, metadata);
    }

}

