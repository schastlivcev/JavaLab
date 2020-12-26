package ru.kpfu.itis.rodsher.bookman.repositories;

import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.bookman.dto.BookDto;
import ru.kpfu.itis.rodsher.bookman.dto.BookRequest;
import ru.kpfu.itis.rodsher.bookman.models.Book;

import java.util.List;
import java.util.stream.Collectors;

import static ru.kpfu.itis.rodsher.bookman.models.QBook.book;

@Repository
public class BooksByRequestRepositoryImpl implements BooksByRequestRepository {

    @Autowired
    private BooksRepository booksRepository;

    @Override
    public List<BookDto> findByRequest(BookRequest bookRequest) {
        BooleanBuilder predicate = new BooleanBuilder();
        if(bookRequest.getTitle() != null) {
            predicate.or(book.title.eq(bookRequest.getTitle()));
        }
        if(bookRequest.getIsbn() != null) {
            predicate.or(book.isbn.eq(bookRequest.getIsbn()));
        }
        if(bookRequest.getFinished() != null) {
            predicate.or(book.finished.eq(bookRequest.getFinished()));
        }
        if(bookRequest.getPublished() != null) {
            predicate.or(book.published.eq(bookRequest.getPublished()));
        }
        List<Book> books = (List<Book>) booksRepository.findAll(predicate);

        return books.stream().map(book -> BookDto.builder()
                .title(book.getTitle())
                .description(book.getDescription())
                .isbn(book.getIsbn())
                .finished(book.getFinished())
                .published(book.getPublished())
                .genres(book.getGenres())
                .build())
                .collect(Collectors.toList());
    }
}
