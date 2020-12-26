package ru.kpfu.itis.rodsher.bookman.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.bookman.models.Book;

@Repository
public interface BooksRepository extends MongoRepository<Book, String>, QuerydslPredicateExecutor<Book> {
    @RestResource(path = "finishedAndPublished", rel = "finishedAndPublished")
    @Query("{published: true, finished: true}")
    Page<Book> findAllPublishedAndFinished(Pageable pageable);
}
