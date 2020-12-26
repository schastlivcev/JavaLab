package ru.kpfu.itis.rodsher.bookman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.rodsher.bookman.models.Genre;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private String title;
    private String description;
    private String isbn;
    private Boolean finished;
    private Boolean published;
    private List<Genre> genres;
}
