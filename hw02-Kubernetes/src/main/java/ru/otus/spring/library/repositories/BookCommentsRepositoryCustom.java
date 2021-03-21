package ru.otus.spring.library.repositories;

import ru.otus.spring.library.models.Comment;

public interface BookCommentsRepositoryCustom {

    Comment addBookComment(long bookId, String cmt);
    public void updateBookComment(long commentId, String cmt);
}
