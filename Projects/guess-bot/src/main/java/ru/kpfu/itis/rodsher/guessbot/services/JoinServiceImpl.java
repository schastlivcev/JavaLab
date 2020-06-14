package ru.kpfu.itis.rodsher.guessbot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.guessbot.dto.Dto;
import ru.kpfu.itis.rodsher.guessbot.dto.Status;
import ru.kpfu.itis.rodsher.guessbot.dto.WebDto;
import ru.kpfu.itis.rodsher.guessbot.models.*;
import ru.kpfu.itis.rodsher.guessbot.repositories.ChannelsRepository;
import ru.kpfu.itis.rodsher.guessbot.repositories.ChannelsToUsersRepository;
import ru.kpfu.itis.rodsher.guessbot.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class JoinServiceImpl implements JoinService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ChannelsRepository channelsRepository;

    @Autowired
    private ChannelsToUsersRepository channelsToUsersRepository;

    @Autowired
    private SenderService senderService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public Dto joinUser(ClientType clientType, String channelId, ChannelType channelType, String userId, String name) {
        Optional<User> userOptional = usersRepository.findByClientTypeAndClientId(clientType, userId);
        if(userOptional.isPresent()) {
            return new WebDto(Status.USER_JOIN_ALREADY_JOINED_ERROR, "name", userOptional.get().getName());
        } else {
            if(name.trim().equals("")) {
                return new WebDto(Status.USER_JOIN_NAME_ERROR);
            } else {
                if(usersRepository.findByName(name).isPresent()) {
                    return new WebDto(Status.USER_JOIN_EXISTS_ERROR);
                }
                User user = usersRepository.save(User.builder().name(name).clientId(userId).clientType(clientType).build());
                if(!channelsRepository.findByClientTypeAndClientId(clientType, channelId).isPresent()) {
                    Channel channel = channelsRepository.save(Channel.builder().clientId(channelId).type(channelType).clientType(clientType).build());
                }
                channelsToUsersRepository.save(ChannelsToUsers.builder().user(user).channel(channelsRepository.findByClientTypeAndClientId(clientType, channelId).get()).build());
                return new WebDto(Status.USER_JOIN_SUCCESS, "users", usersRepository.findAll());
            }
        }
    }

    @Override
    public void disconnectAll() {
        channelsToUsersRepository.deleteAll();
        channelsRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Override
    public void disconnectUser(User user) {
        List<ChannelsToUsers> channelsToUsers = channelsToUsersRepository.findByUser_Id(user.getId());
        channelsToUsersRepository.deleteByUser_Id(user.getId());
        senderService.sendMessageToAll(messageSource.getMessage("game.word.inactivity", new Object[]{user.getName()}, Locale.getDefault()));
        List<Channel> channelsToDelete = new ArrayList<>();
        for(ChannelsToUsers channelToUsers : channelsToUsers) {
            if(channelsToUsersRepository.findByChannel_Id(channelToUsers.getChannel().getId()).isEmpty()) {
                channelsToDelete.add(channelToUsers.getChannel());
            }
        }
        if(!channelsToDelete.isEmpty()) {
            channelsRepository.deleteAll(channelsToDelete);
        }
        usersRepository.delete(user);
    }
}