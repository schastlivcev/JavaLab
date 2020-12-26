package ru.kpfu.itis.rodsher.hateoasproject.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.kpfu.itis.rodsher.hateoasproject.models.Book;

@RepositoryRestResource
public interface BooksRepository extends JpaRepository<Book, Long> {
    @RestResource(path = "finishedAndPublished", rel = "finishedAndPublished")
    @Query("from Book book where book.published = true and book.finished = true")
    Page<Book> findAllPublishedAndFinished(Pageable pageable);
}
