package ru.javalab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @PostMapping("/person")
    public ResponseEntity<?> save(@RequestBody Person person) {
        return ResponseEntity.ok(personService.save(person));
    }

    @GetMapping("/person/{user-id}")
    public ResponseEntity<?> get(@PathVariable("user-id") Long userId) {
        return ResponseEntity.ok(personService.get(userId));
    }
}