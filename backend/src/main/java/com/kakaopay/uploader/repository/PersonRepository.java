package com.kakaopay.uploader.repository;

import com.kakaopay.uploader.domain.Person;

import java.util.List;

public interface PersonRepository {
    void batchInsert(List<Person> list);
    boolean deleteAll();
    int count();
}
