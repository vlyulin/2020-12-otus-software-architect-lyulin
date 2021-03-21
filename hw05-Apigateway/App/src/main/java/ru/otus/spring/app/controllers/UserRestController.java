package ru.otus.spring.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.app.models.User;
import ru.otus.spring.app.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(@Autowired UserService userService) {
        this.userService = userService;
    }
    @RequestMapping(value = "/user/{userId}", headers = "x-userid", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId,
                                           @RequestHeader("x-userid") String xUserdId,
                                           HttpServletRequest request)
    {
        if (userId == 0L) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        try {
            Long xUserIdL = Long.parseLong(xUserdId);
            if (xUserIdL != userId) {
                return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
            }
        } catch( NumberFormatException e){
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");

        if (password == null || firstname == null || password.isEmpty() || firstname.isEmpty()) {
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.getOne(userId);
        if (password != null && !password.isEmpty()) user.setPassword(password);
        if (firstname != null && !firstname.isEmpty()) user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        userService.updateUser(user);

        user = userService.getOne(userId);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{userId}", headers = "x-userid", method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<User> getUser(@PathVariable(value = "userId") Long userId,
                                        @RequestHeader("x-userid") String xUserdId) {

        if (userId == 0L) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        try {
          Long xUserIdL = Long.parseLong(xUserdId);
            if (xUserIdL != userId) {
                return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
            }
        } catch( NumberFormatException e){
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.getOne(userId);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/version")
    public String version() {
        return "1.0.3";
    }
}