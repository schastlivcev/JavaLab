package ru.kpfu.itis.rodsher.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder()
public class FileInfo {
    private Integer id;
    private String storageFileName;
    private String originalFileName;
    private Long size;
    private String type;
    private String url;
    private Integer userId;
}
