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
package com.github.vineey.rql.querydsl.jpa.select;

import com.github.vineey.rql.core.util.CollectionUtils;
import com.github.vineey.rql.querydsl.commons.select.AbstractQuerydslSelectVisitor;
import com.github.vineey.rql.querydsl.commons.select.ProjectionEntry;
import com.github.vineey.rql.querydsl.commons.select.QuerydslSelectParam;
import com.github.vineey.rql.select.parser.ast.SelectNodeList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.querydsl.core.types.*;

import javax.persistence.*;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.vineey.rql.core.util.AnnotationUtils.containsAnnotation;

/**
 * @author vrustia - 5/29/16.
 */
public class JpaQuerydslSelectVisitor extends AbstractQuerydslSelectVisitor<JpaQuerydslSelectParam> {
    @Override
    public Expression visit(SelectNodeList node, JpaQuerydslSelectParam selectParam) {
        List<Path> paths = mapPaths(node, selectParam);

        Map<String, ProjectionEntry> projectionEntryMap = Maps.newHashMap();
        Set<EntityPath> associationPaths = Sets.newHashSet();

        buildProjectionOwnerEntries(paths, selectParam, projectionEntryMap, associationPaths);

        sortProjectionTree(selectParam, projectionEntryMap);

        mapFieldPathToOwnerProjectionEntry(selectParam, paths, projectionEntryMap);

        ProjectionEntry rootProjectionEntry = projectionEntryMap.get(selectParam.getRootPath().toString());

        Expression expression = createProjection(rootProjectionEntry);

        return expression;
    }

    private void mapFieldPathToOwnerProjectionEntry(QuerydslSelectParam selectParam, List<Path> paths, Map<String, ProjectionEntry> projectionEntryMap) {
        EntityPath rootPath = selectParam.getRootPath();
        for (Path path : paths) {

            Path parentEntityPath = path;

            while (parentEntityPath != null && !parentEntityPath.equals(rootPath)) {
                parentEntityPath = parentEntityPath.getMetadata().getParent();
                if (parentEntityPath instanceof EntityPath) {
                    AnnotatedElement annotatedElement = parentEntityPath.getAnnotatedElement();
                    if (containsAnnotation(annotatedElement, OneToOne.class)
                            || containsAnnotation(annotatedElement, ManyToOne.class)
                            || containsAnnotation(annotatedElement, ManyToMany.class)
                            || containsAnnotation(annotatedElement, OneToMany.class)) {

                        ProjectionEntry parentProjectionEntry = projectionEntryMap.get(parentEntityPath.toString());
                        parentProjectionEntry.getFieldPaths().add(path);
                        break;
                    } else if (parentEntityPath.equals(rootPath)) {
                        ProjectionEntry parentProjectionEntry = projectionEntryMap.get(rootPath.toString());
                        parentProjectionEntry.getFieldPaths().add(path);
                        break;
                    }
                }
            }
        }
    }

    private Expression createProjection(ProjectionEntry associationProjectionEntry) {
        EntityPath aliasPath = associationProjectionEntry.getAliasPath();
        EntityPath ownerAssociationPath = associationProjectionEntry.getOwnerPath();
        List<Path> fieldPaths = associationProjectionEntry.getFieldPaths();

        List<Expression> beanExpression = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(fieldPaths)) {
            beanExpression.addAll(fieldPaths);
        }

        List<ProjectionEntry> projectionEntries = associationProjectionEntry.getProjectionEntries();
        if (CollectionUtils.isNotEmpty(projectionEntries)) {
            for (ProjectionEntry projectionEntry : projectionEntries) {
                beanExpression.add(createProjection(projectionEntry));
            }
        }

        QBean bean = Projections.bean(aliasPath, CollectionUtils.isNotEmpty(beanExpression) ? beanExpression.toArray(new Expression[]{}) : new Expression[]{});
        if (associationProjectionEntry.isRootPath()) {
            return bean;
        } else {
            return bean.as(ownerAssociationPath);
        }
    }

    private void sortProjectionTree(QuerydslSelectParam selectParam, Map<String, ProjectionEntry> projectionEntryMap) {
        EntityPath rootPath = selectParam.getRootPath();

        for (ProjectionEntry projectionEntry : projectionEntryMap.values()) {
            Path parentEntityPath = projectionEntry.getOwnerPath();

            while (parentEntityPath != null && !parentEntityPath.equals(rootPath)) {
                parentEntityPath = parentEntityPath.getMetadata().getParent();
                if (parentEntityPath instanceof EntityPath) {
                    AnnotatedElement annotatedElement = parentEntityPath.getAnnotatedElement();
                    if (containsAnnotation(annotatedElement, OneToOne.class)
                            || containsAnnotation(annotatedElement, ManyToOne.class)
                            || containsAnnotation(annotatedElement, ManyToMany.class)
                            || containsAnnotation(annotatedElement, OneToMany.class)) {

                        ProjectionEntry parentProjectionEntry = projectionEntryMap.get(parentEntityPath.toString());
                        parentProjectionEntry.getProjectionEntries().add(projectionEntry);
                        break;
                    } else if (parentEntityPath.equals(rootPath)) {
                        ProjectionEntry parentProjectionEntry = projectionEntryMap.get(parentEntityPath.toString());
                        parentProjectionEntry.getProjectionEntries().add(projectionEntry);
                        break;
                    }
                }
            }
        }
    }

    private void buildProjectionOwnerEntries(List<Path> paths, QuerydslSelectParam selectParam, Map<String, ProjectionEntry> projectionEntryMap, Set<EntityPath> associationPaths) {

        EntityPath rootPath = selectParam.getRootPath();
        Map<EntityPath, EntityPath> joinMapping = selectParam.getJoinMap();

        projectionEntryMap.put(rootPath.toString(),
                new ProjectionEntry()
                        .setOwnerPath(rootPath)
                        .setAliasPath(rootPath)
                        .setRootPath(true));

        for (Path path : paths) {
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

                            projectionEntryMap.put(associationEntityPath.toString(), new ProjectionEntry()
                                    .setOwnerPath((EntityPath) associationEntityPath)
                                    .setAliasPath(aliasPath));

                            associationPaths.add((EntityPath) associationEntityPath);
                        }

                    }
                }

                associationEntityPath = associationEntityPath.getMetadata().getParent();

            }
        }

    }
}
