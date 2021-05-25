package com.kakaopay.uploader.domain;


import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class Person{

    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    @Builder
    public Person(Long id, String firstname, String lastname, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }
}
