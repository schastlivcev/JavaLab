package ru.kpfu.itis.rodsher.guessbot.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.kpfu.itis.rodsher.guessbot.bots.DiscordBot;
import ru.kpfu.itis.rodsher.guessbot.bots.TelegramBot;
import ru.kpfu.itis.rodsher.guessbot.logic.GameLogic;
import ru.kpfu.itis.rodsher.guessbot.logic.GameState;
import ru.kpfu.itis.rodsher.guessbot.repositories.ChannelsRepository;
import ru.kpfu.itis.rodsher.guessbot.repositories.ChannelsToUsersRepository;
import ru.kpfu.itis.rodsher.guessbot.repositories.GamesRepository;
import ru.kpfu.itis.rodsher.guessbot.repositories.UsersRepository;
import ru.kpfu.itis.rodsher.guessbot.services.*;

import javax.security.auth.login.LoginException;
import javax.sql.DataSource;

@Configuration
@ComponentScan("ru.kpfu.itis.rodsher.guessbot")
@EnableScheduling
public class AppConfig {

    @Autowired
    private Environment environment;

    @Bean
    public GameLogic gameLogic(GamesRepository gamesRepository, SenderService senderService, MessageSource messageSource,
                               UsersRepository usersRepository, JoinService joinService) {
        GameLogic gameLogic = new GameLogic(gamesRepository, senderService, usersRepository, joinService, messageSource);
        gameLogic.start();
        return gameLogic;
    }

    @Bean
    public WordService wordService(UsersRepository usersRepository, GameLogic gameLogic, SenderService senderService,
                                   MessageSource messageSource) {
        return new WordServiceImpl(usersRepository, gameLogic, senderService, messageSource);
    }

    @Bean
    public ExitService exitService(UsersRepository usersRepository, ChannelsRepository channelsRepository,
                                   ChannelsToUsersRepository channelsToUsersRepository, GameLogic gameLogic,
                                   SenderService senderService, MessageSource messageSource) {
        return new ExitServiceImpl(usersRepository, channelsRepository,
                 channelsToUsersRepository, gameLogic, senderService, messageSource);
    }

    @Bean
    public JDA discordBot(MessageHandler messageHandler) {
        try {
            return new JDABuilder(environment.getProperty("discord.token")).addEventListeners(
                    new DiscordBot(messageHandler)).setActivity(Activity.playing("/help")).build();
        } catch (LoginException e) {
            throw new IllegalStateException("Failed to connect to Discord.", e);
        }
    }

    @Bean
    public TelegramLongPollingBot telegramBot(MessageHandler messageHandler) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
//            DefaultBotOptions options = new DefaultBotOptions();
            // Proxy
//            options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
//            options.setProxyHost("46.243.183.167");
//            options.setProxyPort(1080);

            TelegramLongPollingBot telegramBot
                    = new TelegramBot(environment.getProperty("telegram.token"), messageHandler);
            telegramBotsApi.registerBot(telegramBot);
            return telegramBot;
        } catch (TelegramApiRequestException e) {
            throw new IllegalStateException("Failed to connect to Telegram.", e);
        }
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        dataSourceBuilder.url("jdbc:sqlite:guess-bot.db3");
        dataSourceBuilder.type(org.sqlite.SQLiteDataSource.class);
        return dataSourceBuilder.build();
    }
}
