package com.archelix.rql.filter.pojo;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vrustia on 9/20/2015.
 */
public class PojoQueryFilterExecutor {
    private List<Predicate> predicates;

    public PojoQueryFilterExecutor(List<Predicate> predicates) {
        this.predicates = predicates;
    }

    public <T> List<T> execute(List<T> dataSource) {
        List<T> items = new ArrayList<>();
        for (T item : dataSource) {
            boolean isFiltered = false;
            for (Predicate predicate : predicates) {
                try {
                    Object property = PropertyUtils.getProperty(item, predicate.getPath());
                    String value = predicate.getValue();
                    if ((value != null && !value.equals(property)) || (value == null && property != null)) {
                        isFiltered = true;
                        break;
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            if (!isFiltered) {
                items.add(item);
            }

        }
        return items;
    }
}
