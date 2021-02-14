package ru.otus.spring.library.docker.config.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.docker.services.SecurityService;

// https://howtodoinjava.com/spring-aop-tutorial/
@Component
@Aspect
public class AclAspects {

    private SecurityService securityService;

    public AclAspects(@Autowired SecurityService securityService) {
        this.securityService = securityService;
    }

    @Before("execution(**  ru.otus.spring.library.docker.repositories.BooksRepository.** (..))")
    public void anyWord() {
        System.out.println("AclAspects: AnyWord");
    }

    @Before("execution(**  ru.otus.spring.library.docker.repositories.BooksRepository.deleteById (..))")
    public void deleteBookAcl(JoinPoint jp) {
        // System.out.println("AclAspects: preciseDefinition: id = " + jp.getArgs()[0]);
        securityService.deleteBookAcl((long)jp.getArgs()[0]);
    }

    @Before("execution(**  ru.otus.spring.library.docker.repositories.BookCommentsRepository.deleteById (..))")
    public void deleteBookCommentAcl(JoinPoint jp) {
        // System.out.println("AclAspects: deleteBookCommentAcl: commentId = " + jp.getArgs()[0]);
        // System.out.println("AclAspects: deleteBookCommentAcl: " + jp.getKind());
        securityService.deleteBookCommentAcl((long)jp.getArgs()[0]);
    }
}
