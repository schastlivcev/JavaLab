package ru.kpfu.itis.rodsher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.dto.WebDto;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.repositories.UsersRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Dto loadUser(long id) {
        Optional<User> userOptional = usersRepository.findById(id);
        if(userOptional.isPresent()) {
            return new WebDto(Status.USER_LOAD_SUCCESS, "user", userOptional.get());
        }
        return new WebDto(Status.USER_LOAD_ERROR);
    }
}
