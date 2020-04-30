package ru.kpfu.itis.rodsher.security.details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.repositories.UsersRepository;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(email == null || email.equals("")) {
            throw new UsernameNotFoundException("Email is empty.");
        }
        if(!email.matches(".+@.+\\..+") || !email.trim().equals(email)) {
            throw new UsernameNotFoundException("Unacceptable email format.");
        }
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserDetailsImpl(user);
        }
        throw new UsernameNotFoundException("User not found.");
    }
}
