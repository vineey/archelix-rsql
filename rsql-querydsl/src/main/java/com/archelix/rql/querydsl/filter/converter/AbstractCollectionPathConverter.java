package com.archelix.rql.querydsl.filter.converter;

import com.archelix.rql.querydsl.filter.UnsupportedRqlOperatorException;
import com.archelix.rql.querydsl.filter.util.ObjectUtil;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.CollectionPathBase;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collection;
import java.util.List;

import static com.archelix.rql.filter.operator.QRSQLOperators.*;

/**
 * @author vrustia - 3/25/16.
 */
public abstract class AbstractCollectionPathConverter<E, Q extends SimpleExpression<? super E>, COLLECTION extends Collection<E>, PATH extends CollectionPathBase<COLLECTION, E, Q>> implements PathConverter<PATH> {
    @Override
    public BooleanExpression evaluate(PATH path, ComparisonNode comparisonNode) {
        ComparisonOperator comparisonOperator = comparisonNode.getOperator();
        String argument = getArgument(comparisonNode);

        if (SIZE_EQ.equals(comparisonOperator)) {
            return path.size().eq(convertToSize(argument));
        } else if (SIZE_NOT_EQ.equals(comparisonOperator)) {
            return path.size().eq(convertToSize(argument)).not();
        }

        throw new UnsupportedRqlOperatorException(comparisonNode, path.getClass());
    }

    private String getArgument(ComparisonNode comparisonNode) {
        List<String> arguments = comparisonNode.getArguments();
        return CollectionUtils.isNotEmpty(arguments) ? arguments.get(0) : null;
    }

    /*protected COLLECTION convertToArgumentList(PATH path, List<String> arguments) {
        if (arguments == null || arguments.size() == 0) {
            return null;
        }
        Class<E> itemClass = path.getElementType();
        Class<COLLECTION> collectionClass = (Class<COLLECTION>) path.getType();
        COLLECTION collectionArgs = newCollection(itemClass);
        for (String arg : arguments) {
            collectionArgs.add(convertArgument(itemClass, arg));
        }
        return collectionArgs;
    }

    protected abstract COLLECTION newCollection(Class<E> pathFieldType);
    */
    private E convertArgument(Class<E> pathFieldType, String arg) {
        return (E) ObjectUtil.convert(pathFieldType, arg);
    }

    private Integer convertToSize(String arg) {
        if (arg == null) {
            throw new IllegalArgumentException("Argument missing for size comparison on a CollectionPathBase");
        }
        return NumberUtils.createInteger(arg);
    }
}
