package ru.otus.spring.library.docker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.library.docker.models.Author;

public interface AuthorsReporitory extends JpaRepository<Author, Long> {
}
