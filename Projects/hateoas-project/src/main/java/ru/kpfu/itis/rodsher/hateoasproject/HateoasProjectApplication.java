package ru.kpfu.itis.rodsher.hateoasproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.kpfu.itis.rodsher.hateoasproject.models.*;
import ru.kpfu.itis.rodsher.hateoasproject.repositories.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@SpringBootApplication
public class HateoasProjectApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(HateoasProjectApplication.class, args);

        UsersRepository usersRepository = context.getBean(UsersRepository.class);
        WallsRepository wallsRepository = context.getBean(WallsRepository.class);
        NotesRepository notesRepository = context.getBean(NotesRepository.class);
        BooksRepository booksRepository = context.getBean(BooksRepository.class);
        GenresRepository genresRepository = context.getBean(GenresRepository.class);
        ChaptersRepository chaptersRepository = context.getBean(ChaptersRepository.class);

        User michael = User.builder()
                .email("mishajava@mail.ru")
                .password("12345678")
                .name("Михаил")
                .surname("Cчастливцев")
                .middleName("Александрович").build();
        User bob = User.builder()
                .email("rodsher111@gmail.com")
                .password("12345678")
                .name("Боб")
                .surname("Бобов")
                .middleName("Бибович").build();
        usersRepository.saveAll(asList(michael, bob));

        Wall mWall = Wall.builder()
                .author(michael)
                .description("Стена Михаила").build();
        Wall bWall = Wall.builder()
                .author(bob)
                .description("Стена Боба").build();
        wallsRepository.saveAll(asList(mWall, bWall));

        Note mNote1 = Note.builder()
                .author(michael)
                .wall(mWall)
                .content("Первая запись").build();
        Note mNote2 = Note.builder()
                .author(michael)
                .wall(mWall)
                .content("Вторая запись").build();
        Note bNote1 = Note.builder()
                .author(bob)
                .wall(bWall)
                .content("Боб начал писать").build();
        notesRepository.saveAll(asList(mNote1, mNote2, bNote1));

        Genre genre1 = Genre.builder()
                .name("Мистика").build();
        Genre genre2 = Genre.builder()
                .name("Триллер").build();
        Genre genre3 = Genre.builder()
                .name("Детектив").build();
        Genre genre4 = Genre.builder()
                .name("Криминал").build();
        genresRepository.saveAll(asList(genre1, genre2, genre3, genre4));

        Book mBook = Book.builder()
                .author(michael)
                .name("Книгант")
                .genres(asList(genre1, genre2))
                .finished(false)
                .published(false)
                .description("Первая и последняя книга").build();
        Book bBook = Book.builder()
                .author(bob)
                .name("Как придумать название для книги")
                .genres(singletonList(genre4))
                .finished(false)
                .published(false)
                .description("Так как же?").build();
        booksRepository.saveAll(asList(mBook, bBook));

        Chapter mChapter1 = Chapter.builder()
                .book(mBook)
                .index("1")
                .heading("Глава 1")
                .content("Начал писать, смотрите шо получилось").build();
        Chapter mChapter2 = Chapter.builder()
                .book(mBook)
                .index("1.2")
                .heading("Глава 2")
                .content("А ниче не получилось, получается").build();
        Chapter bChapter1 = Chapter.builder()
                .book(mBook)
                .index("1")
                .heading("Главище")
                .content("Текстище").build();
        Chapter bChapter2 = Chapter.builder()
                .book(mBook)
                .index("2")
                .heading("Эпилогище")
                .content("Конецище").build();
        chaptersRepository.saveAll(asList(mChapter1, mChapter2, bChapter1, bChapter2));
    }
}
