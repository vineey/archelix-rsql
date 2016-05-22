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

import com.github.vineey.rql.querydsl.test.Application;
import com.github.vineey.rql.querydsl.test.mongo.FongoConfig;
import com.github.vineey.rql.querydsl.test.mongo.entity.Contact;
import com.github.vineey.rql.querydsl.test.mongo.entity.QContact;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongodbQuery;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author vrustia - 5/9/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {FongoConfig.class, Application.class})
public class MongoDaoManualMongoQueryTest {

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("test");

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ContactMongoDao contactMongoDao;

    @Test
    @UsingDataSet(locations = "/testData.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void projections() {
        QContact qcontact = QContact.contact;

        MongodbQuery<Contact> query = new MongodbQuery<>(mongoTemplate, Contact.class);
        List<Contact> contacts = query.fetch(qcontact.id, qcontact.name);

        assertNotNull(contacts);
        assertEquals(3, contacts.size());
        for(Contact contact : contacts) {
            assertNull(contact.getAge());
            assertNull(contact.getAddress());
            assertNull(contact.getEmail());
            assertNull(contact.getGender());
            assertNotNull(contact.getName());
            assertNotNull(contact.getId());
        }
    }
}
