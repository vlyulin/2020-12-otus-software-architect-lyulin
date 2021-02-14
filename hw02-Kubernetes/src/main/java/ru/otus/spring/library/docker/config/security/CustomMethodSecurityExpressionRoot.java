package ru.otus.spring.library.docker.config.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.otus.spring.library.docker.models.Comment;
import ru.otus.spring.library.docker.repositories.BookCommentsRepository;

import java.util.Optional;

public class CustomMethodSecurityExpressionRoot
        extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;

    private BookCommentsRepository bookCommentsRepository;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean isCommentOwner(Long commentId) {

        // пропускаем новые книги
        if( commentId == 0 || commentId == -1 ) return true;

        Optional<Comment> optComment = bookCommentsRepository.findById(commentId);
        if( optComment.isEmpty() ) return false;

        Comment comment = optComment.get();
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        // Если это владелец, то true
        if( login.equals(comment.getCreatedBy().getLogin())) return true;

        return false;
    }

    @Override
    public void setFilterObject(Object o) {
        this.filterObject = o;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object o) {
        this.returnObject = o;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    public void setBookCommentsRepository(BookCommentsRepository bookCommentsRepository) {
        this.bookCommentsRepository = bookCommentsRepository;
    }
}
