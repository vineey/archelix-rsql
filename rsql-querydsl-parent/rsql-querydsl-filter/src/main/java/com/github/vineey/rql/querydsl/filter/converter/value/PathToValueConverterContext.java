/*
* MIT License
*
* Copyright (c) 2016 John Michael Vincent S. Rustia
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
 package com.github.vineey.rql.querydsl.filter.converter.value;

import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.*;

import java.util.Collection;

/**
 * @author vrustia on 9/26/2015.
 */
public final class PathToValueConverterContext {
    private static final StringPathToValueConverter STRING_PATH_CONVERTER = new StringPathToValueConverter();
    private static final EnumPathToValueConverter ENUM_PATH_CONVERTER = new EnumPathToValueConverter();
    private static final NumberPathToValueConverter NUMBER_PATH_CONVERTER = new NumberPathToValueConverter();
    private static final BooleanPathToValueConverter BOOLEAN_PATH_CONVERTER = new BooleanPathToValueConverter();
    private static final TimePathToValueConverter TIME_PATH_CONVERTER = new TimePathToValueConverter();
    private static final DateTimePathToValueConverter DATE_TIME_PATH_CONVERTER = new DateTimePathToValueConverter();
    private static final DatePathToValueConverter DATE_PATH_CONVERTER = new DatePathToValueConverter();
    private static final DefaultCollectionPathToValueConverter<Object, SimpleExpression<? super Object>,
                Collection<Object>, CollectionPathBase<Collection<Object>,
                Object,
                SimpleExpression<? super Object>>> DEFAULT_COLLECTION_PATH_CONVERTER = new DefaultCollectionPathToValueConverter<>();
    private final static ImmutableMap<Class<? extends Path>, PathToValueConverter> map = ImmutableMap.<Class<? extends Path>, PathToValueConverter>builder()
            .put(StringPath.class, STRING_PATH_CONVERTER)
            .put(EnumPath.class, ENUM_PATH_CONVERTER)
            .put(NumberPath.class, NUMBER_PATH_CONVERTER)
            .put(BooleanPath.class, BOOLEAN_PATH_CONVERTER)
            .put(TimePath.class, TIME_PATH_CONVERTER)
            .put(DateTimePath.class, DATE_TIME_PATH_CONVERTER)
            .put(DatePath.class, DATE_PATH_CONVERTER)
            .put(ListPath.class, DEFAULT_COLLECTION_PATH_CONVERTER)
            .put(SetPath.class, DEFAULT_COLLECTION_PATH_CONVERTER)
            .put(CollectionPath.class, DEFAULT_COLLECTION_PATH_CONVERTER)
            .build();

    public static PathToValueConverter getOperator(Path path) {
        return map.get(path.getClass());
    }
}
