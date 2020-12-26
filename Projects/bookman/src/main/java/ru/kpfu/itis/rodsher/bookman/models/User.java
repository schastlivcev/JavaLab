package ru.kpfu.itis.rodsher.bookman.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.kpfu.itis.rodsher.bookman.utils.oid.ObjectIdDeserializer;
import ru.kpfu.itis.rodsher.bookman.utils.oid.ObjectIdSerializer;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private String _id;
    private String name;
    private String surname;
    private String middleName;
    private String email;
    private Date birthDate;
    private Date deathDate;
}
