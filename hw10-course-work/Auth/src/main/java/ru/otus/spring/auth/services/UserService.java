package ru.otus.spring.auth.services;

import ru.otus.spring.auth.models.User;
import ru.otus.spring.auth.services.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    User loadUserByLoginAndPassword(String login, String password) throws UserNotFoundException;
    void register(User newUser);
    User loadUserByLogin(String login) throws UserNotFoundException;
    User getOne(Long id) throws UserNotFoundException;
    void updateUser(User user) throws UserNotFoundException;
    List<User> findAll();
}
