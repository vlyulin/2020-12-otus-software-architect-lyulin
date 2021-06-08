package ru.otus.spring.app.twopc.common.models;

public interface TwoPCResource {
    TwoPCState canCommit(int transactionId);
    TwoPCState preCommit(int transactionId);
    TwoPCState doCommit(int transactionId);
    TwoPCState abort(int transactionId);
    TwoPCState getState();
}
