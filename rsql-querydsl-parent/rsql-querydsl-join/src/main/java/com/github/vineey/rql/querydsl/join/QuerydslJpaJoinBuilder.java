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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.querydsl.core.JoinType;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;

import javax.persistence.*;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.vineey.rql.core.util.AnnotationUtils.containsAnnotation;

/**
 * @author vrustia - 5/28/16.
 */
public class QuerydslJpaJoinBuilder implements QuerydslJoinBuilder<QuerydslJoinParam, JoinEntrySet> {

    @Override
    public JoinEntrySet visit(QuerydslJoinParam querydslJoinParam) {
        List<JoinEntry> joinEntries = Lists.newArrayList();
        Set<EntityPath> associationPaths = Sets.newHashSet();

        EntityPath rootPath = querydslJoinParam.getRootPath();
        Map<EntityPath, EntityPath> joinMapping = querydslJoinParam.getJoinMapping();

        for (Path path : querydslJoinParam.getPaths()) {
            Path associationEntityPath = path;
            while (associationEntityPath != null && !associationEntityPath.equals(rootPath)) {
                if (associationEntityPath instanceof EntityPath) {
                    AnnotatedElement annotatedElement = associationEntityPath.getAnnotatedElement();
                    if (containsAnnotation(annotatedElement, Embedded.class)) {
                        //DO NOTHING
                    } else if (containsAnnotation(annotatedElement, OneToOne.class)
                            || containsAnnotation(annotatedElement, ManyToOne.class)
                            || containsAnnotation(annotatedElement, ManyToMany.class)
                            || containsAnnotation(annotatedElement, OneToMany.class)) {

                        if (!associationPaths.contains(associationEntityPath)) {
                            EntityPath aliasPath = joinMapping.get(associationEntityPath);

                            joinEntries.add(new JoinEntry()
                                    .setAssociationPath((EntityPath) associationEntityPath)
                                    .setAliasPath(aliasPath)
                                    .setJoinType(JoinType.LEFTJOIN));

                            associationPaths.add((EntityPath) associationEntityPath);
                        }

                    }
                }

                associationEntityPath = associationEntityPath.getMetadata().getParent();

            }
        }

        return new JoinEntrySet(joinEntries);
    }

}
