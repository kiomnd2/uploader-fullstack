package com.kakaopay.uploader.repository;

import com.kakaopay.uploader.domain.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class PersonRepositoryImplTest {

    @Autowired
    PersonRepository personRepository;

    @Test
    void batchInsertTest() throws Exception{
        ArrayList<Person> list = new ArrayList<>();
        for (long i=0 ; i<100; i++) {
            Person person = new Person(i, "Fname"+i, "Lname"+i, "kiomnd@email.com");
            list.add(person);
        }
        personRepository.batchInsert(list);

        int count = personRepository.count();
        Assertions.assertThat(count).isEqualTo(list.size());
    }
}
