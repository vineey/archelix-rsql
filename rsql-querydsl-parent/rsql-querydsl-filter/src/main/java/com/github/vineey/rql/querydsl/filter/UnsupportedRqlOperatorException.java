package com.github.vineey.rql.querydsl.filter;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import cz.jirutka.rsql.parser.ast.ComparisonNode;

/**
 * @author vrustia on 9/27/2015.
 */
public class UnsupportedRqlOperatorException extends UnsupportedOperationException {
    public UnsupportedRqlOperatorException(ComparisonNode comparisonNode, Class<? extends Expression> pathClass) {
        super("The comparison operator within expression [" + comparisonNode.toString() + "] is not supported on type " + pathClass.getSimpleName() + ".");
    }
}
