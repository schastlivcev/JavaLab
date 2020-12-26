package ru.kpfu.itis.rodsher.bookman.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private String title;
    private String isbn;
    private Boolean finished;
    private Boolean published;
}
