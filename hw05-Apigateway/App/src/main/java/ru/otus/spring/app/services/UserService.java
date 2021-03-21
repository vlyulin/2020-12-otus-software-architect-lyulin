package ru.otus.spring.app.services;

import ru.otus.spring.app.models.User;
import ru.otus.spring.app.services.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    User loadUserByLoginAndPassword(String login, String password) throws UserNotFoundException;
    void register(User newUser);
    User loadUserByLogin(String login) throws UserNotFoundException;
    User getOne(Long id) throws UserNotFoundException;
    void updateUser(User user) throws UserNotFoundException;
    List<User> findAll();
}
