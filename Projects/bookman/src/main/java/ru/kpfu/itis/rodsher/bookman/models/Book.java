package ru.kpfu.itis.rodsher.bookman.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "books")
public class Book {
    @Id
    private String _id;
    private String title;
    private String description;
    @DBRef
    private User author;
    @DBRef(lazy = true)
    private List<Chapter> chapters;
    @DBRef
    private List<Genre> genres;
    private String isbn;
    private Boolean published;
    private Boolean finished;
    private Date publicationDate;
}