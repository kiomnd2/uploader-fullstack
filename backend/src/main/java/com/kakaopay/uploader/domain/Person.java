package com.kakaopay.uploader.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Person implements Persistable<Long> {
    @Id
    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    @Builder
    public Person(Long id, String firstname, String lastname, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }
}
