package ru.otus.spring.library.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.models.Book;
import ru.otus.spring.library.services.SecurityService;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    @Autowired
    @Lazy
    BooksRepository booksRepository;

    @Autowired
    private SecurityService securityService;

    @Override
    @Transactional
    public void saveBookWithAcl(Book book) {
        booksRepository.save(book);
        securityService.setBookAcl(book);
    }
}
