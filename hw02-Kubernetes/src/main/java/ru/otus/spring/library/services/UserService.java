package ru.otus.spring.library.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.spring.library.models.User;

public interface UserService {
    User loadUserByUsername(String username) throws UsernameNotFoundException;
    User loadUserByLogin(String login) throws UsernameNotFoundException;
    User loadUserById(Long id)  throws UsernameNotFoundException;
}
