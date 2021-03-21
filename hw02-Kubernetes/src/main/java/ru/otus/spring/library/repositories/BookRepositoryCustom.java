package ru.otus.spring.library.repositories;

import ru.otus.spring.library.models.Book;

public interface BookRepositoryCustom {
    void saveBookWithAcl(Book book);
}
