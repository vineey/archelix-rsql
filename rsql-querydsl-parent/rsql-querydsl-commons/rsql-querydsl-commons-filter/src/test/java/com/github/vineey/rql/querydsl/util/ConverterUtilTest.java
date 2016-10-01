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

import com.github.vineey.rql.querydsl.filter.CustomNumber;
import com.github.vineey.rql.querydsl.filter.converter.ConverterConstant;
import com.github.vineey.rql.querydsl.filter.util.ConverterUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Month;

/**
 * @author vrustia - 4/17/16.
 */
@RunWith(JUnit4.class)
public class ConverterUtilTest {

    @BeforeClass
    public static void init() {
        new ConverterUtil();
    }

    @Test
    public void convertAllClass() {
        String argument = "1";
        Class[] classes = new Class[]{Long.class, Integer.class, Double.class, Float.class, BigInteger.class, BigDecimal.class, Short.class};
        for (Class clazz : classes)
            Assert.assertEquals(clazz, ConverterUtil.convert(clazz, argument).getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void convertNumberNotSUpported() {

        ConverterUtil.convert(CustomNumber.class, "1");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void convertNotSUpported() {

        ConverterUtil.convert(Month.class, "1");
    }

    @Test
    public void convertNull() {

        Assert.assertNull(ConverterUtil.convert(Month.class, ConverterConstant.NULL));
    }
}
