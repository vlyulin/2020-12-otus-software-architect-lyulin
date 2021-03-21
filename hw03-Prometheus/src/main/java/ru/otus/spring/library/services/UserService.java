package ru.otus.spring.library.services;

import ru.otus.spring.library.models.User;
import ru.otus.spring.library.services.exceptions.UsernameNotFoundException;

public interface UserService {
    User loadUserByUsername(String username) throws UsernameNotFoundException;
    User loadUserByLogin(String login) throws UsernameNotFoundException;
    User loadUserById(Long id)  throws UsernameNotFoundException;
}
