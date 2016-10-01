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
package com.github.vineey.rql.querydsl.test.mongo.dao;

import com.github.vineey.rql.querydsl.test.mongo.entity.Contact;
import com.github.vineey.rql.querydsl.test.mongo.entity.QAddress;
import com.github.vineey.rql.querydsl.test.mongo.entity.QContact;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Map;

/**
 * @author vrustia - 5/9/16.
 */
public interface ContactMongoDao extends QueryDslPredicateExecutor<Contact>, MongoRepository<Contact, Long> {
    Map<String, Path> PATH_MAP = ImmutableMap.<String, Path>builder()
            .put("contact.id", QContact.contact.id)
            .put("contact.name", QContact.contact.name)
            .put("contact.address", QContact.contact.address)
            .put("contact.address.id", QContact.contact.address.id)
            .put("contact.address.city", QContact.contact.address.city)
            .put("contact.address.country", QContact.contact.address.country)
            .put("contact.age", QContact.contact.age)
            .put("contact.email", QContact.contact.email)
            .put("contact.gender", QContact.contact.gender)
            .build();

    Map<EntityPath, EntityPath> JOIN_MAP = ImmutableMap.<EntityPath, EntityPath>builder()
            .put(QContact.contact.address, QAddress.address1)
            .build();
}
