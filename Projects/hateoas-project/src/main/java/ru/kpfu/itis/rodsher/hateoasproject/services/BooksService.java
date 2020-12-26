package ru.kpfu.itis.rodsher.hateoasproject.services;

import ru.kpfu.itis.rodsher.hateoasproject.models.Book;

public interface BooksService {
    Book setPublished(Long bookId);
    Book setFinished(Long bookId);
}
