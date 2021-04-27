package ru.otus.spring.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.app.models.User;
import ru.otus.spring.app.repositories.UserRepository;
import ru.otus.spring.app.services.exceptions.UserNotFoundException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByLoginAndPassword(String login, String password) throws UserNotFoundException {
        User user = userRepository.findByLoginAndPassword(login, password);
        if (user == null) {
            throw new UserNotFoundException(login + " not found.");
        }
        return user;
    }

    @Override
    public void register(User user) {
        if(user == null) throw new RuntimeException("User is null");
        userRepository.save(user);
        userRepository.flush();
    }

    @Override
    public User loadUserByLogin(String login) throws UserNotFoundException {
        User user = userRepository.findByLogin(login); // .findByNameIgnoreCase(login);
        if (user == null) {
            throw new UserNotFoundException(login + " not found.");
        }
        return user;
    }

    @Override
    public User getOne(Long id) throws UserNotFoundException {
        if( id <= 0 ) throw new RuntimeException("User with id = " + id + " is not found.");
        return userRepository.getOne(id);
    }

    @Override
    public void updateUser(User user) throws UserNotFoundException {
        if(user == null) throw new RuntimeException("User is null");
        userRepository.save(user);
        userRepository.flush();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
