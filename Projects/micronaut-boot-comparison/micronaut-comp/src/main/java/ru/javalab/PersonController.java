package ru.javalab;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.inject.Inject;

@Controller
public class PersonController {

    @Inject
    private PersonService personService;

    @Post("/person")
    public HttpResponse<?> save(@Body Person person) {
        return HttpResponse.ok(personService.save(person));
    }

    @Get("/person/{user-id}")
    public HttpResponse<?> get(@PathVariable("user-id") Long userId) {
        return HttpResponse.ok(personService.get(userId));
    }
}