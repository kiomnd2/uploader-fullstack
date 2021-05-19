package com.kakaopay.uploader.repository;

import com.kakaopay.uploader.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
