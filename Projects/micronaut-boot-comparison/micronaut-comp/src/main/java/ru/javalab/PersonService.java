package ru.javalab;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PersonService {
    @Inject
    private PersonsRepository personsRepository;

    public Person save(Person person) {
        return personsRepository.save(person);
    }

    public Person get(Long userId) {
        return personsRepository.findById(userId).orElseGet( () -> null);
    }
}
