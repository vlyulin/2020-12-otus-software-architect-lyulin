package ru.otus.spring.app.twopc.common.services;

import ru.otus.spring.app.twopc.common.models.TwoPCState;
import ru.otus.spring.app.twopc.common.models.TwoPCTransaction;

public interface TwoPCTransactionManagerService {
    int getNextTransactionId();
    void addTransaction(TwoPCTransaction twoPCTransaction);
    TwoPCState canCommit(int transactionId);
    TwoPCState preCommit(int transactionId);
    TwoPCState doCommit(int transactionId);
    TwoPCState abort(int transactionId);
    void process(int transactionId);
}
