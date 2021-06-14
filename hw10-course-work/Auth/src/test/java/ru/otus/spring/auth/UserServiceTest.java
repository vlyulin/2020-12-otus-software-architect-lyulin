package ru.otus.spring.auth;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.otus.spring.auth.models.User;
import ru.otus.spring.auth.repositories.UserRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Тестирование UserRepository")
@DataJpaTest
@AutoConfigureTestDatabase
// (replace = AutoConfigureTestDatabase.Replace.NONE)
@EntityScan(basePackageClasses = {User.class})
@Import({ru.otus.spring.auth.models.User.class})
@EnableJpaRepositories("ru.otus.spring.auth.repositories")
class UserServiceTest {

    public static final String LAST_USER_01 = "Last User 01";
    public static final String USER_01_LOGIN = "User01";
    public static final String USER_01_PASSWORD = "12345678";

    @Autowired
    UserRepository userRepository;

    @org.junit.jupiter.api.Test
    void loadUserByLoginAndPassword() {
        String login = USER_01_LOGIN;
        String password = USER_01_PASSWORD;
        User user = userRepository.findByLoginAndPassword(login, password);
        assertThat(user).isNotNull().hasFieldOrPropertyWithValue("lastname", LAST_USER_01);
    }
}