package ru.otus.spring.auth.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//import ru.otus.spring.app.models.User;
//import ru.otus.spring.app.services.UserService;

@DisplayName("Тестирование AuthRestController")
@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = {"ru.otus.spring.app"})
class AuthRestControllerTest {

    public static final String SESSION_ID_COOKIE = "session_id";

    @Autowired
    private MockMvc mvc;

    @Autowired
    AuthRestController authRestController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(authRestController).isNotNull();
    }

    @Test
    void login() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/login")
                .param("login","User01")
                .param("password", "12345678")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(mvcResult.getResponse().getHeader("X-UserId")).isNotNull();
        assertThat(mvcResult.getResponse().getHeader("X-User")).isEqualTo("User01");
        assertThat(mvcResult.getResponse().getHeader("X-First-Name")).isEqualTo("User 01");
        assertThat(mvcResult.getResponse().getHeader("X-Last-Name")).isEqualTo("Last User 01");
        assertThat(mvcResult.getResponse().getHeader("X-Email")).isEqualTo("user01@gmail.com");
    }

    @Test
    void notAuthorizedTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/auth");
        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String resp = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void AuthorizationTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/login")
                .param("login","User01")
                .param("password", "12345678")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Cookie session_id = response.getCookie(SESSION_ID_COOKIE);
        assertThat(session_id).isNotNull();

        requestBuilder = MockMvcRequestBuilders
                .get("/auth")
                .cookie(session_id);
        assertThat(mvcResult.getResponse().getHeader("X-UserId")).isNotNull();
    }
}