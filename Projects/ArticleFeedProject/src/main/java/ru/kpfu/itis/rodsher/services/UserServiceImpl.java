package ru.kpfu.itis.rodsher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.dto.WebDto;
import ru.kpfu.itis.rodsher.models.Friends;
import ru.kpfu.itis.rodsher.models.FriendsStatus;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.repositories.FriendsRepository;
import ru.kpfu.itis.rodsher.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FriendsRepository friendsRepository;

    @Override
    public Dto loadUser(long id) {
        Optional<User> userOptional = usersRepository.findById(id);
        if(userOptional.isPresent()) {
            return new WebDto(Status.USER_LOAD_SUCCESS, "user", userOptional.get());
        }
        return new WebDto(Status.USER_LOAD_ERROR);
    }

    @Override
    public Dto loadUsersByNameAndSurname(String name, String surname) {
        return new WebDto(Status.USER_LOAD_BY_NAME_AND_SURNAME_SUCCESS, "users", usersRepository.findByNameAndSurname(name, surname));
    }

    @Override
    public Dto loadFriends(Long userId) {
        return new WebDto(Status.USER_LOAD_FRIENDS_SUCCESS, "friends", friendsRepository.findFriendsForId(userId));
    }

    @Override
    public Dto updateFriendship(Long userSenderId, Long userRecipientId, FriendsStatus status) {
        friendsRepository.saveStatus(new Friends(User.builder().id(userSenderId).build(),
                User.builder().id(userRecipientId).build(), status, null));
        return new WebDto(Status.FRIENDS_UPDATE_STATUS_SUCCESS);
    }

    @Override
    public Dto removeFriendship(Long userSenderId, Long userRecipientId) {
        if(friendsRepository.remove(new Friends(User.builder().id(userSenderId).build(),
                User.builder().id(userRecipientId).build(), null, null))) {
            return new WebDto(Status.FRIENDS_REMOVE_SUCCESS);
        }
        return new WebDto(Status.FRIENDS_REMOVE_ERROR);
    }

    @Override
    public Dto checkFriendship(Long userId1, Long userId2) {
        Optional<Friends> friendsOptional = friendsRepository.find(userId1, userId2);
        if(friendsOptional.isPresent()) {
            return new WebDto(Status.FRIENDSHIP_PRESENTED, "friends", friendsOptional.get());
        }
        return new WebDto(Status.FRIENDSHIP_EMPTY);
    }

    @Override
    public Dto updateUserInfo(User user) {
        if(user.getId() == null) {
            return new WebDto(Status.USER_UPDATE_ERROR);
        }
        boolean updated = usersRepository.updateInfo(user);
        if(updated) {
            return new WebDto(Status.USER_UPDATE_SUCCESS);
        }
        return new WebDto(Status.USER_UPDATE_ERROR);
    }
}
