package com.archelix.rql.querydsl.filter;

import com.mysema.query.annotations.QueryEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

/**
 * @author vrustia - 3/26/16.
 */
@Entity
public class Name {
    @Column
    private String firstname;
    @Column
    private Long age;
    @Column
    private LocalDate date;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
