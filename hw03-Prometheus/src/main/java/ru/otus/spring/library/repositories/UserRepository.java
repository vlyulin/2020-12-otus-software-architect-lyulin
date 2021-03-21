package ru.otus.spring.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.library.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByNameIgnoreCase(String name);
    User findByLoginIgnoreCase(String login);

    @Query(value = "select authority as role from authorities where lower(login) = lower(:login)", nativeQuery = true)
    String[] findAuthoritiesByLogin(@Param("login") String login);

    void deleteById(Long userId);
}
