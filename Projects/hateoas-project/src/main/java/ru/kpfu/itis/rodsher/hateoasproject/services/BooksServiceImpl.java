package ru.kpfu.itis.rodsher.hateoasproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.hateoasproject.models.Book;
import ru.kpfu.itis.rodsher.hateoasproject.repositories.BooksRepository;

import java.util.Optional;

@Service
public class BooksServiceImpl implements BooksService {
    @Autowired
    private BooksRepository booksRepository;

    @Override
    public Book setPublished(Long bookId) {
        Optional<Book> bookOptional = booksRepository.findById(bookId);
        if(bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setPublished(true);
            booksRepository.save(book);
            return book;
        }
        throw new IllegalArgumentException("Book with id = " + bookId + " doesn't exist");
    }

    @Override
    public Book setFinished(Long bookId) {
        Optional<Book> bookOptional = booksRepository.findById(bookId);
        if(bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setFinished(true);
            booksRepository.save(book);
            return book;
        }
        throw new IllegalArgumentException("Book with id = " + bookId + " doesn't exist");
    }
}
