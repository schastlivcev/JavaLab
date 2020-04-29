package ru.kpfu.itis.rodsher.services;

import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.FriendsStatus;
import ru.kpfu.itis.rodsher.models.User;

import java.util.Optional;

public interface UserService {
    Dto loadUser(long id);
    Dto loadUsersByNameAndSurname(String name, String surname);
    Dto loadFriends(Long userId);
    Dto updateFriendship(Long userSenderId, Long userRecipientId, FriendsStatus status);
    Dto removeFriendship(Long userSenderId, Long userRecipientId);
    Dto checkFriendship(Long userId1, Long userId2);
    Dto updateUserInfo(User user);
}
