package ru.otus.spring.library.docker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.library.docker.models.User;
import ru.otus.spring.library.docker.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserRestController {

    private final UserRepository userRepository;

    public UserRestController(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user")
    public long newUser(
            HttpServletRequest request
    ) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        if ( login.isEmpty() || login.isBlank() || password.isEmpty() || password.isBlank() ||
                name.isEmpty() || name.isBlank()
        ) {
            throw new RuntimeException("Not all attributes filled.");
        }

        User chkUser = userRepository.findByLoginIgnoreCase(login);
        if( chkUser != null) {
            throw new RuntimeException("Key (login)=(" + login + ") already exists.");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setName(name);
        userRepository.save(user);

        return user.getId();
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable(value = "userId") Long userId) {
        User user = userRepository.getOne(userId);
        return user;
    }

    @DeleteMapping("/user/{userId}")
    public String deleteUser(@PathVariable(value = "userId") Long userId, HttpServletResponse response) {
        if (userId == 0L) throw new RuntimeException("User ID is not defined");
        userRepository.deleteById(userId);
        response.setStatus(204); // HttpStatus.SC_NO_CONTENT
        return null;
    }

    @PutMapping("/user/{userId}")
    public void updateUser(@PathVariable(value = "userId") Long userId, HttpServletRequest request) {
        if (userId == 0L) throw new RuntimeException("User ID is not defined");

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        if ( login.isEmpty() || login.isBlank() || password.isEmpty() || password.isBlank() ||
                name.isEmpty() || name.isBlank()
        ) {
            throw new RuntimeException("Not all attributes filled.");
        }

        User user = userRepository.getOne(userId);
        if(user == null) {
            throw new RuntimeException("user not found");
        }
        // if(!login.isEmpty() && !login.isBlank()) user.setLogin(login);
        // ERROR: update or delete on table "users" violates foreign key constraint
        // "fk_authorities_users" on table "authorities"
        if(!password.isEmpty() && !password.isBlank()) user.setPassword(password);
        if(!name.isEmpty() && !name.isBlank()) user.setName(name);
        userRepository.save(user);

        return;
    }
}
