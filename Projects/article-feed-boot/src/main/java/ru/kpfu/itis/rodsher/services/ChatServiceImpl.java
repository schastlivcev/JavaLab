package ru.kpfu.itis.rodsher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.dto.WebDto;
import ru.kpfu.itis.rodsher.models.Channel;
import ru.kpfu.itis.rodsher.models.Message;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.repositories.ChannelsRepository;
import ru.kpfu.itis.rodsher.repositories.MessagesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChannelsRepository channelsRepository;

    @Autowired
    private MessagesRepository messagesRepository;


    @Override
    public Dto createChannel(String name) {
        Long id = channelsRepository.save(Channel.builder().name(name).dialogue(true).build());
        return new WebDto(Status.CHANNEL_ADD_SUCCESS, "channel_id", id);
    }

    @Override
    public Dto addUsersToChannel(Long channelId, Long... userIds) {
        for(Long userId : userIds) {
            channelsRepository.addUserToChannel(Channel.builder().id(channelId).build(), User.builder().id(userId).build());
        }
        return new WebDto(Status.CHANNEL_ADD_USER_TO_SUCCESS);
    }

    @Override
    public Dto saveMessage(Long channelId, Long userAuthorId, String content) {
        Long id = messagesRepository.save(Message.builder()
                .channel(Channel.builder().id(channelId).build())
                .author(User.builder().id(userAuthorId).build())
                .content(content)
                .build());
        return new WebDto(Status.MESSAGE_ADD_SUCCESS, "message_id", id);
    }

    @Override
    public Dto loadMessagesForChannel(Long channelId) {
        List<Message> messages = messagesRepository.findMessagesByChannelId(channelId);
        return new WebDto(Status.MESSAGE_LOAD_BY_CHANNEL_ID_SUCCESS, "messages", messages);
    }

    @Override
    public Dto getChannel(Long id) {
        Optional<Channel> channelOptional = channelsRepository.find(id);
        if(channelOptional.isPresent()) {
            return new WebDto(Status.CHANNEL_LOAD_SUCCESS, "channel", channelOptional.get());
        }
        return new WebDto(Status.CHANNEL_LOAD_ERROR);
    }

    @Override
    public Dto getMessage(Long id) {
        Optional<Message> messageOptional = messagesRepository.find(id);
        if(messageOptional.isPresent()) {
            return new WebDto(Status.MESSAGE_LOAD_SUCCESS, "message", messageOptional.get());
        }
        return new WebDto(Status.MESSAGE_LOAD_ERROR);
    }

    @Override
    public Dto checkIfChannelExistsForUsers(Long userId1, Long userId2) {
        List<Channel> channels1 = channelsRepository.findChannelsByUserId(userId1);
        List<Channel> channels2 = channelsRepository.findChannelsByUserId(userId2);
        List<Channel> suitableChannels = new ArrayList<>();
        for(Channel channel1 : channels1) {
            for(Channel channel2 : channels2) {
                if(channel1.getId().equals(channel2.getId())) {
                    suitableChannels.add(channel1);
                }
            }
        }
        if(!suitableChannels.isEmpty()) {
            return new WebDto(Status.CHANNEL_EXISTS_FOR_USERS, "channels", suitableChannels);
        }
        return new WebDto(Status.CHANNEL_NOT_EXISTS_FOR_USERS);
    }

    @Override
    public Dto checkIfUserBelongsToChat(Long userId, Long channelId) {
        if(channelsRepository.checkUserBelongingToChannel(userId, channelId)) {
            return new WebDto(Status.USER_BELONGING_TO_CHANNEL_SUCCESS);
        }
        return new WebDto(Status.USER_BELONGING_TO_CHANNEL_ERROR);
    }

    @Override
    public Dto getUsersForChannel(Long channelId) {
        return new WebDto(Status.CHANNEL_LOAD_USERS_SUCCESS,
                "users", channelsRepository.findUsersForChannelId(channelId));
    }

    @Override
    public Dto loadChannelsByUserId(Long userId) {
        return new WebDto(Status.CHANNEL_LOAD_BY_USER_ID_SUCCESS, "channels", channelsRepository.findChannelsByUserId(userId));
    }

    @Override
    public Dto loadLastChannelMessagesForUserId(Long userId) {
        return new WebDto(Status.MESSAGE_LOAD_LAST_FOR_USER_ID_CHANNELS_SUCCESS, "messages", messagesRepository.findLastMessagesForChannelsByUserId(userId));
    }
}
