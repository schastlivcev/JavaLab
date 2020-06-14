package ru.kpfu.itis.rodsher.guessbot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.guessbot.dto.Dto;
import ru.kpfu.itis.rodsher.guessbot.logic.GameLogic;
import ru.kpfu.itis.rodsher.guessbot.logic.GameState;
import ru.kpfu.itis.rodsher.guessbot.models.ChannelType;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;
import ru.kpfu.itis.rodsher.guessbot.models.User;

import java.util.List;
import java.util.Locale;

@Service
public class MessageHandlerImpl implements MessageHandler {
    @Autowired
    private JoinService joinService;

    @Autowired
    private SenderService senderService;

    @Autowired
    private GameLogic gameLogic;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ExitService exitService;

    @Autowired
    private WordService wordService;

    @Override
    public void handleMessage(ClientType clientType, String channelId, ChannelType channelType, String userId, String message) {
        if(message.startsWith("/")) {
            if(message.startsWith("/join")) {
                String name = "";
                if(message.matches("/join .*")) {
                    name = message.substring("/join ".length());
                }
                Dto dto = joinService.joinUser(clientType, channelId, channelType, userId, name);
                switch (dto.getStatus()) {
                    case USER_JOIN_SUCCESS:
                        if(gameLogic.getGameState().equals(GameState.GAME_NOT_YET_STARTED)) {
                            senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.join.start",
                                    new Object[]{name}, Locale.getDefault()));
                            gameLogic.setGameState(GameState.GAME_CREATED);
                            System.out.println("Received Message[client=" + clientType + ", channel=" + channelId + ", channelType=" + channelType + ", userId=" + userId + ", text=" + message + "] to join by name=" + name + ", started the game.");
                        } else {
                            List<User> users = (List<User>) dto.get("users");
                            StringBuilder playerNames = new StringBuilder();
                            for(int i = 0; i < users.size(); i++) {
                                playerNames.append(users.get(i).getName()).append(" (").append(users.get(i).getClientType().equals(ClientType.DISCORD) ? "d" : "t").append(")");
                                if(i != users.size() - 1) {
                                    playerNames.append(", ");
                                }
                            }
                            senderService.sendMessageToAll(messageSource.getMessage("command.join.success",
                                    new Object[]{name, playerNames}, Locale.getDefault()));
                            System.out.println("Received Message[client=" + clientType + ", channel=" + channelId + ", channelType=" + channelType + ", userId=" + userId + ", text=" + message + "] to join by name=" + name + ", joined the game.");
                        }
                        break;
                    case USER_JOIN_ALREADY_JOINED_ERROR:
                        senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.join.already",
                                new Object[]{dto.get("name")}, Locale.getDefault()));
                        System.out.println("Received Message[client=" + clientType + ", channel=" + channelId + ", channelType=" + channelType + ", userId=" + userId + ", text=" + message + "] to join by name=" + name + ", but already joined.");
                        break;
                    case USER_JOIN_EXISTS_ERROR:
                        senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.join.exists",
                                new Object[]{name}, Locale.getDefault()));
                        System.out.println("Received Message[client=" + clientType + ", channel=" + channelId + ", channelType=" + channelType + ", userId=" + userId + ", text=" + message + "] to join by name=" + name + ", but player with such name is already playing.");
                        break;
                    case USER_JOIN_NAME_ERROR:
                        senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.join.empty",
                                new Object[]{name}, Locale.getDefault()));
                        System.out.println("Received Message[client=" + clientType + ", channel=" + channelId + ", channelType=" + channelType + ", userId=" + userId + ", text=" + message + "] to join by name=" + name + ", but unacceptable name.");
                        break;
                }
            } else if(message.trim().equals("/help")) {
                senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.help", null, Locale.getDefault()));
                System.out.println("Received Message[client=" + clientType + ", channel=" + channelId + ", channelType=" + channelType + ", userId=" + userId + ", text=" + message + "] to help");
            } else if(message.trim().equals("/exit")) {
                System.out.println("#####" + Thread.currentThread().getName() + "Received Message[client=" + clientType + ", channel=" + channelId + ", channelType=" + channelType + ", userId=" + userId + ", text=" + message + "] to exit");
                exitService.exitUser(clientType, channelId, channelType, userId);
            } else if(message.startsWith("/word")) {
                System.out.println("Received Message[client=" + clientType + ", channel=" + channelId + ", channelType=" + channelType + ", userId=" + userId + ", text=" + message + "] to add word");
                String word = "";
                if(message.matches("/word .*")) {
                    word = message.substring("/word ".length());
                }
                wordService.handleWord(clientType, channelId, channelType, userId, word);
            } else if(message.startsWith("/desc")) {
                System.out.println("Received Message[client=" + clientType + ", channel=" + channelId + ", channelType=" + channelType + ", userId=" + userId + ", text=" + message + "] to add desc");
                String description = "";
                if(message.matches("/desc .*")) {
                    description = message.substring("/desc ".length());
                }
                wordService.handleDescription(clientType, channelId, channelType, userId, description);
            } else if(message.startsWith("/guess")) {
                System.out.println("Received Message[client=" + clientType + ", channel=" + channelId + ", channelType=" + channelType + ", userId=" + userId + ", text=" + message + "] to guess");
                String guess = "";
                if(message.matches("/guess .*")) {
                    guess = message.substring("/guess ".length());
                }
                wordService.handleGuess(clientType, channelId, channelType, userId, guess);
            }
        }
    }
}