package com.archelix.rql.filter.operator;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;

/**
 * @author vrustia - 3/26/16.
 */
public final class QRSQLOperators {
    public static final ComparisonOperator SIZE_EQ = new ComparisonOperator("=size=");
    public static final ComparisonOperator SIZE_NOT_EQ = new ComparisonOperator("=sizene=");
    private final static Logger LOGGER = LoggerFactory.getLogger(QRSQLOperators.class);

    public static Set<ComparisonOperator> getOperators() {
        Set<ComparisonOperator> comparisonOperators = RSQLOperators.defaultOperators();
        comparisonOperators.addAll(Arrays.asList(SIZE_EQ, SIZE_NOT_EQ));

        LOGGER.debug("Default Rsql Operators : {}", ArrayUtils.toString(comparisonOperators));

        return comparisonOperators;
    }
}
