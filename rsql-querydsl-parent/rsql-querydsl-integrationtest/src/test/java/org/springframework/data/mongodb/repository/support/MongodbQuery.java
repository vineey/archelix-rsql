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
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* 
*/
package org.springframework.data.mongodb.repository.support;

import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * @author vrustia - 5/22/16.
 */
public class MongodbQuery<T> extends SpringDataMongodbQuery<T> {
    public MongodbQuery(MongoOperations operations, Class<? extends T> type) {
        super(operations, type);
    }

    public MongodbQuery(MongoOperations operations, Class<? extends T> type, String collectionName) {
        super(operations, type, collectionName);
    }

    @Override
    public SpringDataMongodbQuery<T> orderBy(OrderSpecifier<?> o) {
        return super.orderBy(o);
    }

    @Override
    public MongodbQuery<T> orderBy(OrderSpecifier<?>... o) {
        return (MongodbQuery) super.orderBy(o);
    }

    @Override
    public MongodbQuery<T> restrict(QueryModifiers modifiers) {
        return (MongodbQuery) super.restrict(modifiers);
    }


    @Override
    public MongodbQuery<T> where(Predicate e) {
        return (MongodbQuery) super.where(e);
    }

    @Override
    public MongodbQuery<T> where(Predicate... e) {
        return (MongodbQuery) super.where(e);
    }
}
