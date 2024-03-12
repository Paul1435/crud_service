package ru.test.task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.test.task.models.User;
import ru.test.task.repositories.UserRepository;
import ru.test.task.security.ImpUserDetails;

import java.util.Optional;

@Service
public class ImpUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public ImpUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUserName(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new ImpUserDetails(user.get());
    }
}
