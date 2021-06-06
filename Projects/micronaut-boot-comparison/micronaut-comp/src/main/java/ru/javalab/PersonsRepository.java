package ru.javalab;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface PersonsRepository extends JpaRepository<Person, Long> {
}