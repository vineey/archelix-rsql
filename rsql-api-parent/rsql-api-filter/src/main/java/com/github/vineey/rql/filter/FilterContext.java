package com.github.vineey.rql.filter;

/**
 * @author vrustia on 9/26/2015.
 */
public class FilterContext<T, E extends FilterParam> {
    private FilterBuilder<T, E> filterBuilder;
    private E filterParam;

    public static <T, E extends FilterParam> FilterContext<T, E> withBuilderAndParam(FilterBuilder<T, E> builder, E filterParam) {
        return new FilterContext<T, E>()
                .setFilterBuilder(builder)
                .setFilterParam(filterParam);
    }

    public FilterBuilder<T, E> getFilterBuilder() {
        return filterBuilder;
    }

    public FilterContext setFilterBuilder(FilterBuilder<T, E> filterBuilder) {
        this.filterBuilder = filterBuilder;
        return this;
    }

    public E getFilterParam() {
        return filterParam;
    }

    public FilterContext setFilterParam(E filterParam) {
        this.filterParam = filterParam;
        return this;
    }
}
