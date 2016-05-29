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
package com.github.vineey.rql.querydsl.test.jpa.dao;

import com.github.vineey.rql.querydsl.test.jpa.entity.*;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Map;

/**
 * @author vrustia - 5/29/16.
 */
public interface BookDao extends QueryDslPredicateExecutor<Book>, JpaRepository<Book, Long> {
    Map<EntityPath, EntityPath> JOIN_MAP = ImmutableMap.<EntityPath, EntityPath>builder()
            .put(QBook.book.publisher, QPublisher.publisher)
            .build();

    Map<String, Path> PATH_MAP = ImmutableMap.<String, Path>builder()
            .put("book.id", QBook.book.id)
            .put("book.name", QBook.book.name)
            .put("book.author", QBook.book.author)
            .put("book.publisherId", QBook.book.publisherId)
            .put("book.publisher", QBook.book.publisher)
            .put("book.publisher.id", QBook.book.publisher.id)
            .put("book.publisher.name", QBook.book.publisher.name)
            .build();
}
