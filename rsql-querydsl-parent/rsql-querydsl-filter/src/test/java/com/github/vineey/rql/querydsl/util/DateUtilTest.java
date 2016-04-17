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
package com.github.vineey.rql.querydsl.util;

import com.github.vineey.rql.querydsl.filter.util.DateUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;

/**
 * @author vrustia - 4/17/16.
 */
@RunWith(JUnit4.class)
public class DateUtilTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void init(){
        new DateUtil();
    }

    @Test
    public void emptyTime() {

        Assert.assertNull(DateUtil.parseLocalTime(""));
    }


    @Test
    public void emptyDateTime() {

        Assert.assertNull(DateUtil.parseLocalDateTime(""));
    }

    @Test
    public void localDateTime_DateOnly() {

        LocalDateTime localDateTime = DateUtil.parseLocalDateTime("2014-01-01");
        Assert.assertNotNull(localDateTime);
        Assert.assertEquals("2014-01-01 00:00:00", localDateTime.format(DateUtil.LOCAL_DATE_TIME_FORMATTER));
    }


    @Test
    public void emptyDate() {

        Assert.assertNull(DateUtil.parseLocalDate(""));
    }
}
