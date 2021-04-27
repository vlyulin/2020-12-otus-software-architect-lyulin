package ru.otus.spring.app.repositories.exceptions;

public class VersionsIsNotMatchException extends RuntimeException {
    public VersionsIsNotMatchException(String message) {
        super(message);
    }
}
