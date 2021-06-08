package ru.otus.spring.app.order.server.repositories.exceptions;

public class VersionsIsNotMatchException extends RuntimeException {
    public VersionsIsNotMatchException(String message) {
        super(message);
    }
}
