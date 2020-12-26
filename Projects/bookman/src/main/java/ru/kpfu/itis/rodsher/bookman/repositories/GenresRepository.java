package ru.kpfu.itis.rodsher.bookman.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.bookman.models.Genre;

@Repository
public interface GenresRepository extends MongoRepository<Genre, String> {
}
