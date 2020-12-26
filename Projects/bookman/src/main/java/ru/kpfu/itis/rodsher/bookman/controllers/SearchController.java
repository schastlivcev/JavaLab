package ru.kpfu.itis.rodsher.bookman.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.rodsher.bookman.dto.BookDto;
import ru.kpfu.itis.rodsher.bookman.dto.BookRequest;
import ru.kpfu.itis.rodsher.bookman.repositories.BooksByRequestRepository;

import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private BooksByRequestRepository booksByRequestRepository;

    @GetMapping("/books/searchBy")
    public ResponseEntity<List<BookDto>> searchByRequest(BookRequest bookRequest) {
        return ResponseEntity.ok(booksByRequestRepository.findByRequest(bookRequest));
    }
}