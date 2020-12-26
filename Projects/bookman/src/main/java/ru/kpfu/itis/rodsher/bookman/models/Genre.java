package ru.kpfu.itis.rodsher.bookman.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "genres")
public class Genre {
    @Id
    private String _id;
    private String title;
    private String description;
}
