package ru.kpfu.itis.rodsher.bookman.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chapters")
public class Chapter {
    @Id
    private String _id;
    @DBRef
    private Book book;
    private String index;
    private String heading;
    private String content;
}
