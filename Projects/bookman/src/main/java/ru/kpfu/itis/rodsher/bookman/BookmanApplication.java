package ru.kpfu.itis.rodsher.bookman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.kpfu.itis.rodsher.bookman.models.Book;
import ru.kpfu.itis.rodsher.bookman.models.Chapter;
import ru.kpfu.itis.rodsher.bookman.models.Genre;
import ru.kpfu.itis.rodsher.bookman.models.User;
import ru.kpfu.itis.rodsher.bookman.repositories.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@SpringBootApplication
public class BookmanApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BookmanApplication.class, args);
//        UsersDriverOrTemplateRepository repository = context.getBean(UsersTemplateRepositoryImpl.class);
//        User user = repository.find("5fe721ca01bb7c59ac555781").get();
//        System.out.println(user);
//        User user1 = User.builder()
//                .name("Боба")
//                .surname("Биба")
//                .middleName("Баба")
//                .email("boba@mail.ru")
//                .birthDate(new Date(LocalDate.of(1999, 12, 8).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()))
//                .deathDate(new Date(LocalDate.of(2999, 12, 8).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()))
//                .build();
//        repository.save(user1);
//        repository.delete("5fe73cef5f429d0e711e3d1c");

//        UsersRepository usersRepository = context.getBean(UsersRepository.class);
//        User michael = User.builder()
//                .email("mishajava@mail.ru")
//                .name("Михаил")
//                .surname("Cчастливцев")
//                .middleName("Александрович").build();
//        User bob = User.builder()
//                .email("rodsher111@gmail.com")
//                .name("Боб")
//                .surname("Бобов")
//                .middleName("Бибович").build();
//        usersRepository.saveAll(asList(michael, bob));
//
//        GenresRepository genresRepository = context.getBean(GenresRepository.class);
//        Genre genre1 = Genre.builder()
//                .title("Мистика").build();
//        Genre genre2 = Genre.builder()
//                .title("Триллер").build();
//        Genre genre3 = Genre.builder()
//                .title("Детектив").build();
//        Genre genre4 = Genre.builder()
//                .title("Криминал").build();
//        genresRepository.saveAll(asList(genre1, genre2, genre3, genre4));
//
//        BooksRepository booksRepository = context.getBean(BooksRepository.class);
//        Book mBook = Book.builder()
//                .author(michael)
//                .title("Книгант")
//                .genres(asList(genre1, genre2))
//                .finished(true)
//                .published(true)
//                .description("Первая и последняя книга").build();
//        Book bBook = Book.builder()
//                .author(bob)
//                .title("Как придумать название для книги")
//                .genres(singletonList(genre4))
//                .finished(true)
//                .published(false)
//                .description("Так как же?").build();
//        booksRepository.saveAll(asList(mBook, bBook));
//
//        ChaptersRepository chaptersRepository = context.getBean(ChaptersRepository.class);
//        Chapter mChapter1 = Chapter.builder()
//                .book(mBook)
//                .index("1")
//                .heading("Глава 1")
//                .content("Начал писать, смотрите шо получилось").build();
//        Chapter mChapter2 = Chapter.builder()
//                .book(mBook)
//                .index("1.2")
//                .heading("Глава 2")
//                .content("А ниче не получилось, получается").build();
//        Chapter bChapter1 = Chapter.builder()
//                .book(mBook)
//                .index("1")
//                .heading("Главище")
//                .content("Текстище").build();
//        Chapter bChapter2 = Chapter.builder()
//                .book(mBook)
//                .index("2")
//                .heading("Эпилогище")
//                .content("Концище").build();
//        chaptersRepository.saveAll(asList(mChapter1, mChapter2, bChapter1, bChapter2));


    }
}
