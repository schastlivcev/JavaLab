package ru.kpfu.itis.rodsher.bookman;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import ru.kpfu.itis.rodsher.bookman.models.User;
import ru.kpfu.itis.rodsher.bookman.repositories.UsersDriverOrTemplateRepository;
import ru.kpfu.itis.rodsher.bookman.repositories.UsersDriverRepositoryImpl;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class MongoDriverTest {
    public static void main(String[] args) throws JsonProcessingException {
        UsersDriverOrTemplateRepository repository = new UsersDriverRepositoryImpl();
//        User user1 = User.builder()
//                ._id("5fe721ca01bb7c59ac555781")
//                .name("Боб")
//                .surname("Биб")
//                .middleName("Баб")
//                .email("bob@mail.ru")
//                .birthDate(new Date(LocalDate.of(1999, 12, 8).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()))
//                .deathDate(new Date(LocalDate.of(2999, 12, 8).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()))
//                .build();

//        repository.save(user1);

//        List<User> userList = repository.findAll();
//        for(User user : userList) {
//            System.out.println(user);
//        }

        User found = repository.find("5fe721ca01bb7c59ac555781").get();
        found.setDeathDate(new Date(LocalDate.of(3999, 12, 8).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()));
        repository.update(found);

        //repository.delete("5fe721ca01bb7c59ac555781");
    }
}
