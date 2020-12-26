package ru.kpfu.itis.rodsher.hateoasproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.hateoasproject.controllers.BooksController;
import ru.kpfu.itis.rodsher.hateoasproject.models.Book;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookFinishRepresentationProcessor implements RepresentationModelProcessor<EntityModel<Book>> {
    @Autowired
    private RepositoryEntityLinks links;

    @Override
    public EntityModel<Book> process(EntityModel<Book> model) {
        model.add(linkTo(methodOn(BooksController.class).finish(model.getContent().getId())).withRel("finish"));
        return model;
    }
}