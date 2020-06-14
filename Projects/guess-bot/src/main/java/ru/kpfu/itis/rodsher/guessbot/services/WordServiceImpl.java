package ru.kpfu.itis.rodsher.guessbot.services;

import org.springframework.context.MessageSource;
import ru.kpfu.itis.rodsher.guessbot.logic.GameLogic;
import ru.kpfu.itis.rodsher.guessbot.logic.GameState;
import ru.kpfu.itis.rodsher.guessbot.models.ChannelType;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;
import ru.kpfu.itis.rodsher.guessbot.models.User;
import ru.kpfu.itis.rodsher.guessbot.repositories.UsersRepository;

import java.util.Locale;
import java.util.Optional;

public class WordServiceImpl implements WordService {

    private UsersRepository usersRepository;

    private GameLogic gameLogic;

    private SenderService senderService;

    private MessageSource messageSource;

    public WordServiceImpl(UsersRepository usersRepository, GameLogic gameLogic, SenderService senderService, MessageSource messageSource) {
        this.usersRepository = usersRepository;
        this.gameLogic = gameLogic;
        this.senderService = senderService;
        this.messageSource = messageSource;
    }

    @Override
    public void handleWord(ClientType clientType, String channelId, ChannelType channelType, String clientId, String word) {
        if(channelType.equals(ChannelType.GROUP)) {
            senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.word.unallowed", null, Locale.getDefault()));
        } else {
            if(gameLogic.getGameState().equals(GameState.GAME_WORDING)) {
                Optional<User> userOptional = usersRepository.findByClientTypeAndClientId(clientType, clientId);
                if(userOptional.isPresent()) {
                    User user = userOptional.get();
                    if(gameLogic.getLeader().getId().equals(user.getId())) {
                        if(word.trim().equals("")) {
                            senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.word.wrong", null, Locale.getDefault()));
                        } else {
                            if(gameLogic.getDescription() != null) {
                                if(gameLogic.getDescription().toLowerCase().contains(word.trim().toLowerCase())) {
                                    senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.word.contain", null, Locale.getDefault()));
                                } else {
                                    gameLogic.setWord(word.toLowerCase());
                                    senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.word.desc", new Object[]{word.toLowerCase()}, Locale.getDefault()));
                                    gameLogic.removeTimerDelay();
                                }
                            } else {
                                gameLogic.setWord(word.toLowerCase());
                                senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.word.success", new Object[]{word.toLowerCase()}, Locale.getDefault()));
                            }
                        }
                    } else {
                        senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.word.leader", new Object[]{user.getName()}, Locale.getDefault()));
                    }
                }
            } else {
                senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.word.state", null, Locale.getDefault()));
            }
        }
    }

    @Override
    public void handleDescription(ClientType clientType, String channelId, ChannelType channelType, String clientId, String description) {
        if(channelType.equals(ChannelType.GROUP)) {
            senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.desc.unallowed", null, Locale.getDefault()));
        } else {
            if(gameLogic.getGameState().equals(GameState.GAME_WORDING)) {
                Optional<User> userOptional = usersRepository.findByClientTypeAndClientId(clientType, clientId);
                if(userOptional.isPresent()) {
                    User user = userOptional.get();
                    if(gameLogic.getLeader().getId().equals(user.getId())) {
                        if(description.trim().equals("")) {
                            senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.desc.wrong", null, Locale.getDefault()));
                        } else {
                            if(gameLogic.getWord() != null) {
                                if(description.toLowerCase().contains(gameLogic.getWord())) {
                                    senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.desc.contain", null, Locale.getDefault()));
                                } else {
                                    gameLogic.setDescription(description);
                                    senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.desc.word", new Object[]{description}, Locale.getDefault()));
                                    gameLogic.removeTimerDelay();
                                }
                            } else {
                                gameLogic.setDescription(description);
                                senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.desc.success", new Object[]{description}, Locale.getDefault()));
                            }
                        }
                    } else {
                        senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.desc.leader", new Object[]{user.getName()}, Locale.getDefault()));
                    }
                }
            } else {
                senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.desc.state", null, Locale.getDefault()));
            }
        }
    }

    @Override
    public void handleGuess(ClientType clientType, String channelId, ChannelType channelType, String clientId, String guess) {
        if(gameLogic.getGameState().equals(GameState.GAME_GUESSING)) {
            Optional<User> userOptional = usersRepository.findByClientTypeAndClientId(clientType, clientId);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                if(guess.trim().equals("")) {
                    senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.guess.wrong", null, Locale.getDefault()));
                } else {
                    if(gameLogic.getLeader().getId().equals(user.getId())) {
                        if(guess.toLowerCase().trim().equals(gameLogic.getWord().trim())) {
                            if(channelType.equals(ChannelType.GROUP)) {
                                senderService.sendMessageToAll(messageSource.getMessage("command.guess.leader.group", new Object[]{user.getName(), guess.trim()}, Locale.getDefault()));
                                gameLogic.setWinner(user);
                                gameLogic.setGameState(GameState.GAME_ENDED);
                            } else if(channelType.equals(ChannelType.PRIVATE)) {
                                senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.guess.leader.private", new Object[]{user.getName()}, Locale.getDefault()));
                            }
                        } else {
                            senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.guess.leader", new Object[]{user.getName()}, Locale.getDefault()));
                        }
                    } else {
                        if(gameLogic.getWord().trim().equals(guess.trim())) {
                            senderService.sendMessageToAll(messageSource.getMessage("command.guess.success", new Object[]{user.getName(), guess.trim()}, Locale.getDefault()));
                            gameLogic.setWinner(user);
                            gameLogic.setGameState(GameState.GAME_ENDED);
                        } else {
                            senderService.sendMessageToAllExcluding(clientType, channelId, messageSource.getMessage("command.guess", new Object[]{user.getName(), guess}, Locale.getDefault()));
                        }
                    }
                }
            }
        } else {
            senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.guess.state", null, Locale.getDefault()));
        }
    }
}
