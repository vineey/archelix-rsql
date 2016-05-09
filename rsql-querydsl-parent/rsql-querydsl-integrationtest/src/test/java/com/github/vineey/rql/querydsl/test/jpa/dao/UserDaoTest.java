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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.vineey.rql.RqlInput;
import com.github.vineey.rql.core.util.StringUtils;
import com.github.vineey.rql.querydsl.DefaultQuerydslRqlParser;
import com.github.vineey.rql.querydsl.QuerydslMappingParam;
import com.github.vineey.rql.querydsl.QuerydslMappingResult;
import com.github.vineey.rql.querydsl.QuerydslRqlParser;
import com.github.vineey.rql.querydsl.test.jpa.JpaApplication;
import com.github.vineey.rql.querydsl.test.jpa.entity.User;
import com.mysema.query.QueryModifiers;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.OrderSpecifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.github.vineey.rql.querydsl.test.jpa.entity.QUser.user;
import static org.junit.Assert.*;

/**
 * @author vrustia - 5/9/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JpaApplication.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup(value = "/testData.xml")
public class UserDaoTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void rqlJpa() {
        String select = "select(user.id, user.name)";
        String rqlFilter = "user.name == 'A*'";
        String limit = "limit(0, 10)";
        String sort = "sort(+user.id)";

        RqlInput rqlInput = new RqlInput()
                .setSelect(select)
                .setFilter(rqlFilter)
                .setLimit(limit)
                .setSort(sort);

        QuerydslRqlParser querydslRqlParser = new DefaultQuerydslRqlParser();

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setRootPath(user).setPathMapping(UserDao.PATH_MAP));

        JPAQuery jpaQuery = new JPAQuery(entityManager);
        QueryModifiers page = querydslMappingResult.getPage();
        List<User> users = jpaQuery.from(user)
                .where(querydslMappingResult.getPredicate())
                .offset(page.getOffset())
                .limit(page.getLimit())
                .orderBy(querydslMappingResult.getOrderSpecifiers().toArray(new OrderSpecifier[]{}))
                .list(querydslMappingResult.getSelect());

        assertNotNull(users);
        assertEquals(1, users.size());
        User user = users.get(0);

        assertNotNull(user.getName());
        assertNull(user.getBday());
        assertNull(user.getUsername());
    }

    @Test
    public void rqlJpaAll() {
        String limit = "limit(0, 10)";
        String sort = "sort(-user.id)";

        RqlInput rqlInput = new RqlInput()
                .setLimit(limit)
                .setSort(sort);

        QuerydslRqlParser querydslRqlParser = new DefaultQuerydslRqlParser();

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setRootPath(user).setPathMapping(UserDao.PATH_MAP));

        JPAQuery jpaQuery = new JPAQuery(entityManager);
        QueryModifiers page = querydslMappingResult.getPage();
        List<User> users = jpaQuery.from(user)
                .offset(page.getOffset())
                .limit(page.getLimit())
                .orderBy(querydslMappingResult.getOrderSpecifiers().toArray(new OrderSpecifier[]{}))
                .list(querydslMappingResult.getSelect());

        assertNotNull(users);
        assertEquals(3, users.size());
        long id = 3L;
        for(User user : users) {
            assertEquals(id, user.getId());
            assertNotNull(user.getBday());
            assertTrue(StringUtils.isNotEmpty(user.getUsername()));
            assertNotNull(StringUtils.isNotEmpty(user.getName()));
            id--;
        }
    }
}
