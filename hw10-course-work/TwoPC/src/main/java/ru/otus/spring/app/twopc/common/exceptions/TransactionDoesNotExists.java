package ru.otus.spring.app.twopc.common.exceptions;

public class TransactionDoesNotExists extends RuntimeException {
    public TransactionDoesNotExists(String message) {
        super(message);
    }
}
