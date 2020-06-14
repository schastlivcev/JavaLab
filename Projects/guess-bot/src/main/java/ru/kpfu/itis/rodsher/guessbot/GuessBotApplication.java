package ru.kpfu.itis.rodsher.guessbot;

import net.dv8tion.jda.api.JDA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.kpfu.itis.rodsher.guessbot.models.User;
import ru.kpfu.itis.rodsher.guessbot.repositories.UsersRepository;

import java.util.Locale;

@SpringBootApplication
public class GuessBotApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        ConfigurableApplicationContext context = SpringApplication.run(GuessBotApplication.class, args);
    }

}
