package ru.otus.spring.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;
import ru.otus.spring.auth.models.User;
import ru.otus.spring.auth.services.UserService;
import ru.otus.spring.auth.services.exceptions.UserNotFoundException;
//import ru.otus.spring.app.models.User;
//import ru.otus.spring.app.services.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class AuthRestController {

    public static final String SESSION_ID_COOKIE = "session_id";
    private UserService userService = null;
    private Map<UUID, User> sessions = new HashMap<>();

    public AuthRestController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void newUser(
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {
        PrintWriter out = response.getWriter();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");

        if (login == null || password == null || firstname == null || login.isEmpty() || password.isEmpty() || firstname.isEmpty()) {
            // throw new RuntimeException("Not all attributes filled.");
            response.setStatus(HttpStatus.OK.value());
            out.print("{\"status\": \"error\", \"message\": \"Not all attributes filled.\"}");
            return;
        }

        User user = null;
        try {
            userService.loadUserByLogin(login);
            // throw new RuntimeException("User with " + login + " already exists.");
            response.setStatus(HttpStatus.OK.value());
            out.print("{\"status\": \"error\", \"message\": \"User with \"" + login + "\" already exists.\"}");
            return;
        } catch (UserNotFoundException e) {
        }

        user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        userService.register(user);

        out.print("{\"status\": \"ok\", \"userId\": \"" + user.getId() + "\"}");
        return;
    }

    @PostMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Bad login or password");
            return;
        }

        PrintWriter out = response.getWriter();
        User user;
        try {
            user = userService.loadUserByLoginAndPassword(login, password);
        } catch (Exception e) {
            out.print("{\"status\": \"error\",\"message\": \"" + e.getMessage() + "\"}");
            return;
        }

        if (user == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Bad login or password");
            return;
        }

        UUID uuid = UUID.randomUUID();
        sessions.put(uuid, user);
        response.addHeader("X-UserId",String.valueOf(user.getId()));
        response.addHeader("X-User",user.getLogin());
        response.addHeader("X-First-Name",user.getFirstname());
        response.addHeader("X-Last-Name",user.getLastname());
        response.addHeader("X-Email",user.getEmail());
        String uuid_str = uuid.toString();
        response.addCookie(new Cookie(SESSION_ID_COOKIE,uuid.toString()));
        response.setStatus(HttpStatus.OK.value());

        out.print("{\"status\": \"ok\"}");
    }

    @GetMapping("/auth")
    public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Cookie session_id_cookie = WebUtils.getCookie(request, SESSION_ID_COOKIE);
        if(session_id_cookie == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
            return;
        }

        UUID uuid = UUID.fromString(session_id_cookie.getValue());
        if( !sessions.containsKey(uuid) ) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
            return;
        }

        User user = sessions.get(uuid);
        if (user == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized. User is null");
            return;
        }
        response.addHeader("X-UserId",String.valueOf(user.getId()));
        response.addHeader("X-User",user.getLogin());
        response.addHeader("X-First-Name",user.getFirstname());
        response.addHeader("X-Last-Name",user.getLastname());
        response.addHeader("X-Email",user.getEmail());
        return;
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie(SESSION_ID_COOKIE,"");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);

        response.setStatus(HttpStatus.OK.value());
        PrintWriter out = response.getWriter();
        out.print("{\"status\": \"ok\"}");
    }

    @GetMapping("/signin")
    public void signin(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setStatus(HttpStatus.OK.value());
        PrintWriter out = response.getWriter();
        out.print("{\"status\": \"error\", \"message\": \"Please go to login and provide Login/Password\"}");
    }

    @GetMapping("/sessions")
    public Map<UUID,User> getSessions () {
        return sessions;
    }

    @GetMapping("/auth-version")
    public String version() {
        return "1.0.0";
    }
}
