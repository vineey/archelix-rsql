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
import com.github.vineey.rql.querydsl.*;
import com.github.vineey.rql.querydsl.join.JoinEntry;
import com.github.vineey.rql.querydsl.test.Application;
import com.github.vineey.rql.querydsl.test.jpa.entity.Book;
import com.github.vineey.rql.querydsl.test.jpa.entity.QBook;
import com.github.vineey.rql.querydsl.test.jpa.entity.User;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
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

import static com.github.vineey.rql.querydsl.test.jpa.entity.QBook.book;
import static com.github.vineey.rql.querydsl.test.jpa.entity.QUser.user;
import static org.junit.Assert.*;

/**
 * @author vrustia - 5/9/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup(value = "/testData.xml")
public class UserDaoRqlTest {

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

        QuerydslRqlParser querydslRqlParser = new JpaQuerydslRqlParser();

        QuerydslMappingResult querydslMappingResult =
                querydslRqlParser.parse(rqlInput,
                        new QuerydslMappingParam()
                                .setRootPath(user)
                                .setPathMapping(UserDao.PATH_MAP));

        JPAQuery<User> jpaQuery = new JPAQuery(entityManager);
        QueryModifiers page = querydslMappingResult.getPage();

        List<User> users = jpaQuery
                .select(Projections.constructor(User.class, querydslMappingResult.getProjection())).from(user)
                .where(querydslMappingResult.getPredicate())
                .offset(page.getOffset())
                .limit(page.getLimit())
                .orderBy(querydslMappingResult.getOrderSpecifiers().toArray(new OrderSpecifier[]{}))
                .fetch();

        assertNotNull(users);
        assertEquals(1, users.size());
        User user = users.get(0);

        assertNotNull(user.getName());
        assertNull(user.getBday());
        assertNull(user.getUsername());
    }


    @Test
    public void getBooksWithProjectionsAndJoins() {
        String select = "select(book.id, book.name, book.publisher.id, book.publisher.name)";
        String rqlFilter = "book.author == 'batman'";
        String limit = "limit(0, 10)";
        String sort = "sort(+book.id)";

        RqlInput rqlInput = new RqlInput()
                .setSelect(select)
                .setFilter(rqlFilter)
                .setLimit(limit)
                .setSort(sort);

        QuerydslRqlParser querydslRqlParser = new JpaQuerydslRqlParser();

        QuerydslMappingResult querydslMappingResult =
                querydslRqlParser.parse(rqlInput,
                        new QuerydslMappingParam()
                                .setRootPath(book)
                                .setPathMapping(BookDao.PATH_MAP)
                                .setJoinMapping(BookDao.JOIN_MAP));


        QueryModifiers page = querydslMappingResult.getPage();

        JPAQuery<Book> jpaQuery = new JPAQuery(entityManager);

        jpaQuery = (JPAQuery<Book>)jpaQuery
                .select(querydslMappingResult.getProjection())
                .from(book)
                .where(querydslMappingResult.getPredicate());

        jpaQuery = buildJoin(querydslMappingResult, jpaQuery);

        List<Book> books = jpaQuery
                .offset(page.getOffset())
                .limit(page.getLimit())
                .orderBy(querydslMappingResult.getOrderSpecifiers().toArray(new OrderSpecifier[]{}))
                .fetch();

        assertNotNull(books);
        assertEquals(2, books.size());
        Book gothamCity = books.get(0);
        assertNotNull(gothamCity.getId());
        assertNotNull(gothamCity.getName());
        assertNotNull(gothamCity.getPublisher());
        assertNotNull(gothamCity.getPublisher().getId());
        assertNotNull(gothamCity.getPublisher().getName());

        assertNull(gothamCity.getAuthor());
        assertNull(gothamCity.getPublisherId());
    }


    private JPAQuery<Book> buildJoin(QuerydslMappingResult querydslMappingResult, JPAQuery<Book> jpaQuery) {
        for (JoinEntry joinEntry : querydslMappingResult.getJoinListNode().getList()) {
            JoinType joinType = joinEntry.getJoinType();
            EntityPath associationPath = joinEntry.getAssociationPath();
            EntityPath aliasPath = joinEntry.getAliasPath();
            switch (joinType) {
                case INNERJOIN:
                case JOIN:
                    jpaQuery = jpaQuery.innerJoin(associationPath, aliasPath);
                    break;
                case RIGHTJOIN:
                    jpaQuery = jpaQuery.rightJoin(associationPath, aliasPath);
                    break;
                case LEFTJOIN:
                    jpaQuery = jpaQuery.leftJoin(associationPath, aliasPath);
                    break;
                case FULLJOIN:
                case DEFAULT:
                default:

            }
        }
        return jpaQuery;
    }

    @Test
    public void rqlJpaAll() {
        String limit = "limit(0, 10)";
        String sort = "sort(-user.id)";

        RqlInput rqlInput = new RqlInput()
                .setLimit(limit)
                .setSort(sort);

        QuerydslRqlParser querydslRqlParser = new JpaQuerydslRqlParser();

        QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput, new QuerydslMappingParam().setRootPath(user).setPathMapping(UserDao.PATH_MAP));

        JPAQuery<User> jpaQuery = new JPAQuery(entityManager);
        QueryModifiers page = querydslMappingResult.getPage();

        List<User> users = jpaQuery.select(Projections.constructor(User.class, querydslMappingResult.getProjection())).from(user)
                .where(querydslMappingResult.getPredicate())
                .offset(page.getOffset())
                .limit(page.getLimit())
                .orderBy(querydslMappingResult.getOrderSpecifiers().toArray(new OrderSpecifier[]{}))
                .fetch();


        assertNotNull(users);
        assertEquals(3, users.size());
        Long id = 3L;
        for (User user : users) {
            assertEquals(id, user.getId());
            assertNotNull(user.getBday());
            assertTrue(StringUtils.isNotEmpty(user.getUsername()));
            assertNotNull(StringUtils.isNotEmpty(user.getName()));
            id--;
        }
    }

}
