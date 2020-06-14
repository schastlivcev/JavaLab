package ru.kpfu.itis.rodsher.guessbot.services;

import org.springframework.context.MessageSource;
import ru.kpfu.itis.rodsher.guessbot.logic.GameLogic;
import ru.kpfu.itis.rodsher.guessbot.logic.GameState;
import ru.kpfu.itis.rodsher.guessbot.models.*;
import ru.kpfu.itis.rodsher.guessbot.repositories.ChannelsRepository;
import ru.kpfu.itis.rodsher.guessbot.repositories.ChannelsToUsersRepository;
import ru.kpfu.itis.rodsher.guessbot.repositories.UsersRepository;

import java.util.*;

public class ExitServiceImpl implements ExitService {

    private UsersRepository usersRepository;

    private ChannelsRepository channelsRepository;

    private ChannelsToUsersRepository channelsToUsersRepository;

    private GameLogic gameLogic;

    private SenderService senderService;

    private MessageSource messageSource;

    public ExitServiceImpl(UsersRepository usersRepository, ChannelsRepository channelsRepository,
                           ChannelsToUsersRepository channelsToUsersRepository, GameLogic gameLogic,
                           SenderService senderService, MessageSource messageSource) {
        this.usersRepository = usersRepository;
        this.channelsRepository = channelsRepository;
        this.channelsToUsersRepository = channelsToUsersRepository;
        this.gameLogic = gameLogic;
        this.senderService = senderService;
        this.messageSource = messageSource;
    }

    @Override
    public void exitUser(ClientType clientType, String channelId, ChannelType channelType, String clientId) {
        Optional<User> userOptional = usersRepository.findByClientTypeAndClientId(clientType, clientId);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            List<ChannelsToUsers> channelsToUsers = channelsToUsersRepository.findByUser_Id(user.getId());
            channelsToUsersRepository.deleteByUser_Id(user.getId());
            senderService.sendMessageToAll(messageSource.getMessage("command.exit.success", new Object[]{user.getName()}, Locale.getDefault()));
            if(!channelsRepository.findByClientTypeAndClientId(clientType, channelId).isPresent()) {
                senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.exit.success", new Object[]{user.getName()}, Locale.getDefault()));
            }
            List<Channel> channelsToDelete = new ArrayList<>();
            for(ChannelsToUsers channelToUsers : channelsToUsers) {
                if(channelsToUsersRepository.findByChannel_Id(channelToUsers.getChannel().getId()).isEmpty()) {
                    channelsToDelete.add(channelToUsers.getChannel());
                }
            }
            if(channelsToUsersRepository.findAll().isEmpty()) {
                System.out.println("Game ended due to everyone left.");
                switch (gameLogic.getGameState()) {
                    case GAME_CREATED:
                        senderService.sendMessageToAll(messageSource.getMessage("game.start.empty", new Object[]{user.getName()}, Locale.getDefault()));
                        if(!channelsRepository.findByClientTypeAndClientId(clientType, channelId).isPresent()) {
                            senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("game.start.empty", new Object[]{user.getName()}, Locale.getDefault()));
                        }
                        gameLogic.setGameState(GameState.GAME_NOT_YET_STARTED);
                        break;
                    case GAME_CHOOSING:
                    case GAME_WORDING:
                    case GAME_GUESSING:
                        senderService.sendMessageToAll(messageSource.getMessage("game.end.empty", new Object[]{user.getName()}, Locale.getDefault()));
                        if(!channelsRepository.findByClientTypeAndClientId(clientType, channelId).isPresent()) {
                            senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("game.end.empty", new Object[]{user.getName()}, Locale.getDefault()));
                        }
                        gameLogic.setGameState(GameState.GAME_NOT_YET_STARTED);
                        break;
                    default:
                        System.out.println("EXIT_EVERYONE_DEFAULT_CASE");
                        break;
                }
            }
            if(!channelsToDelete.isEmpty()) {
                channelsRepository.deleteAll(channelsToDelete);
            }
            usersRepository.delete(user);
            if(gameLogic.getGameState().equals(GameState.GAME_CHOOSING) || gameLogic.getGameState().equals(GameState.GAME_WORDING)) {
                if(gameLogic.getLeader().getId().equals(user.getId())) {
                    senderService.sendMessageToAll(messageSource.getMessage("command.exit.leader", new Object[]{user.getName()}, Locale.getDefault()));
                    gameLogic.setLeader(null);
                    gameLogic.removeTimerDelay();
//                    gameLogic.setGameState(GameState.GAME_CHOOSING);
                }
            } else if(gameLogic.getGameState().equals(GameState.GAME_GUESSING) && usersRepository.findAll().size() == 1) {
                gameLogic.setGameState(GameState.GAME_NOT_YET_STARTED);
            }
        } else {
            senderService.sendDirectMessage(clientType, channelId, channelType, messageSource.getMessage("command.exit.error", null, Locale.getDefault()));
        }
    }
}