package com.github.vineey.rql.querydsl.filter.converter;

import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.CollectionPathBase;

import java.util.Collection;

/**
 * @author vrustia - 3/26/16.
 */
public class DefaultCollectionPathConverter<E, Q extends SimpleExpression<? super E>, COLLECTION extends Collection<E>, PATH extends CollectionPathBase<COLLECTION, E, Q>> extends AbstractCollectionPathConverter<E, Q, COLLECTION, PATH> {
}
