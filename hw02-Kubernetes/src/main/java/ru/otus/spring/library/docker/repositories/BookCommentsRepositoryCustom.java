package ru.otus.spring.library.docker.repositories;

import ru.otus.spring.library.docker.models.Comment;

public interface BookCommentsRepositoryCustom {

    Comment addBookComment(long bookId, String cmt);
    public void updateBookComment(long commentId, String cmt);
}
