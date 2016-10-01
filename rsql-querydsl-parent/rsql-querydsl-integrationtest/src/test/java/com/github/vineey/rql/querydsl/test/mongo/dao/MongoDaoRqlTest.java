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

import com.github.vineey.rql.RqlInput;
import com.github.vineey.rql.core.util.StringUtils;
import com.github.vineey.rql.querydsl.QuerydslMappingParam;
import com.github.vineey.rql.querydsl.QuerydslMappingResult;
import com.github.vineey.rql.querydsl.QuerydslRqlParser;
import com.github.vineey.rql.querydsl.mongodb.parser.MongoQuerydslMappingParam;
import com.github.vineey.rql.querydsl.mongodb.parser.MongoQuerydslMappingResult;
import com.github.vineey.rql.querydsl.mongodb.parser.MongoQuerydslRqlParser;
import com.github.vineey.rql.querydsl.mongo.select.MongoQueryUtil;
import com.github.vineey.rql.querydsl.spring.SpringUtil;
import com.github.vineey.rql.querydsl.test.Application;
import com.github.vineey.rql.querydsl.test.mongo.entity.Address;
import com.github.vineey.rql.querydsl.test.mongo.entity.Contact;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongodbQuery;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.github.vineey.rql.querydsl.test.jpa.entity.QUser.user;
import static com.github.vineey.rql.querydsl.test.mongo.dao.ContactMongoDao.JOIN_MAP;
import static com.github.vineey.rql.querydsl.test.mongo.dao.ContactMongoDao.PATH_MAP;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.*;

/**
 * @author vrustia - 5/9/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MongoTestConfig.class, Application.class})
@Ignore
public class MongoDaoRqlTest {

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("test");

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ContactMongoDao contactMongoDao;

    @Test
    @UsingDataSet(locations = {"testData.json"}, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void rqlMongo() {
        String rqlFilter = "contact.name == 'A*'";
        String limit = "limit(0, 10)";
        String sort = "sort(+contact.id)";

        RqlInput rqlInput = new RqlInput()
                .setFilter(rqlFilter)
                .setLimit(limit)
                .setSort(sort);

        QuerydslRqlParser querydslRqlParser = new MongoQuerydslRqlParser();

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setRootPath(user).setPathMapping(PATH_MAP).setJoinMapping(JOIN_MAP));

        QueryModifiers page = querydslMappingResult.getPage();

        List<OrderSpecifier> orderSpecifiers = querydslMappingResult.getOrderSpecifiers();

        Page<Contact> contactPage = contactMongoDao.findAll(querydslMappingResult.getPredicate(), SpringUtil.toPageable(orderSpecifiers, page));

        List<Contact> contactList = contactPage.getContent();

        assertNotNull(contactList);
        assertEquals(1, contactList.size());
        Contact contact = contactList.get(0);
        assertEquals(1L, contact.getId().longValue());

        Address address = contact.getAddress();
        assertNotNull(address);
        assertNotNull(address.getCountry());
        assertNotNull(address.getCity());
        assertEquals("Philippines", address.getCountry());
        assertEquals("Angeles", address.getCity());
    }

    @Test
    @UsingDataSet(locations = "/testData.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void rqlMongoAll() {
        String limit = "limit(0, 10)";
        String sort = "sort(-contact.id)";

        RqlInput rqlInput = new RqlInput()
                .setLimit(limit)
                .setSort(sort);

        QuerydslRqlParser querydslRqlParser = new MongoQuerydslRqlParser();

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setRootPath(user).setPathMapping(PATH_MAP).setJoinMapping(JOIN_MAP));

        List<OrderSpecifier> orderSpecifiers = querydslMappingResult.getOrderSpecifiers();
        QueryModifiers page = querydslMappingResult.getPage();
        Page<Contact> contactPage = contactMongoDao.findAll(SpringUtil.toPageable(orderSpecifiers, page));

        List<Contact> contacts = contactPage.getContent();
        assertNotNull(contacts);
        assertEquals(3, contacts.size());
        long id = 3L;
        for (Contact contact : contacts) {
            assertEquals(id, contact.getId().longValue());
            assertTrue(StringUtils.isNotEmpty(contact.getName()));
            assertTrue(StringUtils.isNotEmpty(contact.getEmail()));
            Address address = contact.getAddress();
            assertNotNull(address);
            assertNotNull(address.getCountry());
            assertNotNull(address.getCity());
            assertEquals("Philippines", address.getCountry());
            assertEquals("Angeles", address.getCity());
            assertNotNull(contact.getGender());
            assertNotNull(contact.getAge());
            id--;
        }
    }

    @Test
    @UsingDataSet(locations = "/testData.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void rqlWithProjections() {
        String select = "select(contact.id, contact.name, contact.address)";
        String rqlFilter = "contact.name == 'A*'";
        String limit = "limit(0, 10)";
        String sort = "sort(+contact.id)";

        RqlInput rqlInput = new RqlInput()
                .setSelect(select)
                .setFilter(rqlFilter)
                .setLimit(limit)
                .setSort(sort);

        MongoQuerydslRqlParser querydslRqlParser = new MongoQuerydslRqlParser();

        MongoQuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new MongoQuerydslMappingParam().setRootPath(user).setPathMapping(PATH_MAP).setJoinMapping(JOIN_MAP));

        QueryModifiers page = querydslMappingResult.getPage();

        Path[] projectedPaths = MongoQueryUtil.toMongodbPaths(querydslMappingResult.getProjection());

        OrderSpecifier[] sortOrders = querydslMappingResult.getOrderSpecifiers().toArray(new OrderSpecifier[]{});

        MongodbQuery<Contact> query = new MongodbQuery<>(mongoTemplate, Contact.class);
        List<Contact> contacts = query.where(querydslMappingResult.getPredicate())
                .restrict(page)
                .orderBy(sortOrders)
                .fetch(projectedPaths);

        assertNotNull(contacts);
        assertEquals(1, contacts.size());
        Contact contact = contacts.get(0);
        assertNull(contact.getAge());
        assertNull(contact.getEmail());
        assertNull(contact.getGender());
        assertNotNull(contact.getId());
        assertNotNull(contact.getName());
        Address address = contact.getAddress();
        assertNotNull(address);
        assertNotNull(address.getCountry());
        assertNotNull(address.getCity());
        assertEquals("Philippines", address.getCountry());
        assertEquals("Angeles", address.getCity());
    }

    @Test
    @UsingDataSet(locations = {"/testData.json"}, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void rqlFindByAddress() {
        String rqlFilter = "contact.address.id == 1";
        String limit = "limit(0, 10)";
        String sort = "sort(+contact.id)";

        RqlInput rqlInput = new RqlInput()
                .setFilter(rqlFilter)
                .setLimit(limit)
                .setSort(sort);

        MongoQuerydslRqlParser querydslRqlParser = new MongoQuerydslRqlParser();

        MongoQuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new MongoQuerydslMappingParam().setRootPath(user).setPathMapping(PATH_MAP).setJoinMapping(JOIN_MAP));

        QueryModifiers page = querydslMappingResult.getPage();

        List<OrderSpecifier> orderSpecifiers = querydslMappingResult.getOrderSpecifiers();

        Page<Contact> contactPage = contactMongoDao.findAll(querydslMappingResult.getPredicate(), SpringUtil.toPageable(orderSpecifiers, page));

        List<Contact> contacts = contactPage.getContent();
        assertNotNull(contacts);
        assertEquals(3, contacts.size());
        long id = 1L;
        for (Contact contact : contacts) {
            assertEquals(id, contact.getId().longValue());
            assertTrue(StringUtils.isNotEmpty(contact.getName()));
            assertTrue(StringUtils.isNotEmpty(contact.getEmail()));
            Address address = contact.getAddress();
            assertNotNull(address);
            assertNotNull(address.getCountry());
            assertNotNull(address.getCity());
            assertEquals("Philippines", address.getCountry());
            assertEquals("Angeles", address.getCity());
            assertNotNull(contact.getGender());
            assertNotNull(contact.getAge());
            id++;
        }


    }

}
