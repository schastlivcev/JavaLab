package ru.kpfu.itis.rodsher.hateoasproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kpfu.itis.rodsher.hateoasproject.models.Book;
import ru.kpfu.itis.rodsher.hateoasproject.services.BooksService;

@RepositoryRestController
public class BooksController {
    @Autowired
    private BooksService booksService;

    @PutMapping("/books/{book-id}/publish")
    @ResponseBody
    public EntityModel<Book> publish(@PathVariable("book-id") Long bookId) {
        return EntityModel.of(booksService.setPublished(bookId));
    }

    @PutMapping("/books/{book-id}/finish")
    @ResponseBody
    public EntityModel<Book> finish(@PathVariable("book-id") Long bookId) {
        return EntityModel.of(booksService.setFinished(bookId));
    }
}