package ru.otus.spring.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.library.models.PublishingHouse;

public interface PublishingHousesRepository extends JpaRepository<PublishingHouse, Long> {
}
