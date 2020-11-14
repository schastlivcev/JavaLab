package ru.kpfu.itis.rodsher.html;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Form {
    private String name;
    private String action;
    private String method;
}
