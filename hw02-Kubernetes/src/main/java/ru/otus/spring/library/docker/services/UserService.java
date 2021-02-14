package ru.otus.spring.library.docker.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.spring.library.docker.models.User;

public interface UserService {
    User loadUserByUsername(String username) throws UsernameNotFoundException;
    User loadUserByLogin(String login) throws UsernameNotFoundException;
    User loadUserById(Long id)  throws UsernameNotFoundException;
}
