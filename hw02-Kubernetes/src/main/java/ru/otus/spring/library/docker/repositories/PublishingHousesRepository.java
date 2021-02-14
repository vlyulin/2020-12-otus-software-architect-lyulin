package ru.otus.spring.library.docker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.library.docker.models.PublishingHouse;

public interface PublishingHousesRepository extends JpaRepository<PublishingHouse, Long> {
}
