package ru.kpfu.itis.rodsher.bookman.repositories;

import ru.kpfu.itis.rodsher.bookman.dto.BookDto;
import ru.kpfu.itis.rodsher.bookman.dto.BookRequest;

import java.util.List;

public interface BooksByRequestRepository {
    List<BookDto> findByRequest(BookRequest bookRequest);
}
