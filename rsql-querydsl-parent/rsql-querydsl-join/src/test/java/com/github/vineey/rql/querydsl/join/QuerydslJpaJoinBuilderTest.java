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
package com.github.vineey.rql.querydsl.join;

import com.github.vineey.rql.querydsl.test.jpa.QAccount;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.querydsl.core.JoinType;
import com.querydsl.core.types.EntityPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.vineey.rql.querydsl.test.jpa.QEmployee.employee;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author vrustia - 5/29/16.
 */
@RunWith(JUnit4.class)
public class QuerydslJpaJoinBuilderTest {
    private QuerydslJpaJoinBuilder joinBuilder;

    private Map<EntityPath, EntityPath> JOIN_MAP = ImmutableMap.<EntityPath, EntityPath>builder()
            .put(employee.account, QAccount.account)
            .build();

    @Before
    public void init() {
        joinBuilder = new QuerydslJpaJoinBuilder();
    }

    @Test
    public void getJoins() {
        QuerydslJoinParam querydslJoinParam = new QuerydslJoinParam()
                .setJoinMapping(JOIN_MAP)
                .setRootPath(employee)
                .setPaths(Sets.newHashSet(
                        employee.employeeNumber,
                        employee.account.id,
                        employee.account.username));

        JoinEntrySet joinEntrySet = joinBuilder.visit(querydslJoinParam);
        assertNotNull(joinEntrySet);
        List<JoinEntry> joinEntries = joinEntrySet.getList();
        assertNotNull(joinEntries);
        assertEquals(1, joinEntries.size());

        JoinEntry accountJoinEntry = joinEntries.get(0);
        assertEquals(JoinType.LEFTJOIN, accountJoinEntry.getJoinType());
        assertEquals(employee.account, accountJoinEntry.getAssociationPath());
        assertEquals(QAccount.account, accountJoinEntry.getAliasPath());


    }
}
