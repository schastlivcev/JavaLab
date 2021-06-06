package ru.javalab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonsRepository personsRepository;

    public Person save(Person person) {
        return personsRepository.save(person);
    }

    public Person get(Long userId) {
        return personsRepository.findById(userId).orElseGet( () -> null);
    }
}
