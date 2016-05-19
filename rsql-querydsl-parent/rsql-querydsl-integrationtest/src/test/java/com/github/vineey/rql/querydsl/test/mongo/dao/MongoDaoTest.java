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
import com.github.vineey.rql.querydsl.DefaultQuerydslRqlParser;
import com.github.vineey.rql.querydsl.QuerydslMappingParam;
import com.github.vineey.rql.querydsl.QuerydslMappingResult;
import com.github.vineey.rql.querydsl.QuerydslRqlParser;
import com.github.vineey.rql.querydsl.spring.SpringUtil;
import com.github.vineey.rql.querydsl.test.Application;
import com.github.vineey.rql.querydsl.test.mongo.FongoConfig;
import com.github.vineey.rql.querydsl.test.mongo.entity.Contact;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.OrderSpecifier;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.github.vineey.rql.querydsl.test.jpa.entity.QUser.user;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.*;

/**
 * @author vrustia - 5/9/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {FongoConfig.class, Application.class})
public class MongoDaoTest {

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("test");

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ContactMongoDao contactMongoDao;

    @Test
    @UsingDataSet(locations = {"/testData.json"}, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void rqlMongo() {
        String rqlFilter = "contact.name == 'A*'";
        String limit = "limit(0, 10)";
        String sort = "sort(+contact.id)";

        RqlInput rqlInput = new RqlInput()
                .setFilter(rqlFilter)
                .setLimit(limit)
                .setSort(sort);

        QuerydslRqlParser querydslRqlParser = new DefaultQuerydslRqlParser();

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setRootPath(user).setPathMapping(ContactMongoDao.PATH_MAP));

        QueryModifiers page = querydslMappingResult.getPage();

        List<OrderSpecifier> orderSpecifiers = querydslMappingResult.getOrderSpecifiers();

        Page<Contact> contactPage = contactMongoDao.findAll(querydslMappingResult.getPredicate(), SpringUtil.toPageable(orderSpecifiers, page));
        List<Contact> contactList = contactPage.getContent();

        assertNotNull(contactList);
        assertEquals(1, contactList.size());
        assertEquals(1L, contactList.get(0).getId().longValue());

    }

    @Test
    @UsingDataSet(locations = "/testData.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void rqlMongoAll() {
        String limit = "limit(0, 10)";
        String sort = "sort(-contact.id)";

        RqlInput rqlInput = new RqlInput()
                .setLimit(limit)
                .setSort(sort);

        QuerydslRqlParser querydslRqlParser = new DefaultQuerydslRqlParser();

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setRootPath(user).setPathMapping(ContactMongoDao.PATH_MAP));

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
            assertTrue(StringUtils.isNotEmpty(contact.getAddress()));
            assertNotNull(contact.getGender());
            assertNotNull(contact.getAge());
            id--;
        }
    }
}
