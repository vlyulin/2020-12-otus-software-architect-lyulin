package ru.otus.spring.app.twopc.common.services;

import ru.otus.spring.app.twopc.common.models.TwoPCState;
import ru.otus.spring.app.twopc.common.models.TwoPCTransaction;

import java.util.*;
import java.util.logging.Logger;

public class TwoPCTransactionManagerServiceImpl implements TwoPCTransactionManagerService {

    Logger log = Logger.getLogger(TwoPCTransactionManagerServiceImpl.class.getName());

    private int transactionIdCounter = 0;
    private Map<Integer, TwoPCTransaction> transactions = new HashMap<>();

    @Override
    public int getNextTransactionId() {
        return ++transactionIdCounter;
    }

    @Override
    public void addTransaction(TwoPCTransaction twoPCTransaction) {
        transactions.put(twoPCTransaction.getTransactionId(), twoPCTransaction);
    }

    @Override
    public TwoPCState canCommit(int transactionId) {
        if(!transactions.containsKey(transactionId)) {
            throw new RuntimeException("Transaction ID " + transactionId +" doesn't exist.");
        }
        TwoPCTransaction twoPCTransaction = transactions.get(transactionId);
        return twoPCTransaction.canCommit();
    }

    @Override
    public TwoPCState preCommit(int transactionId) {
        if(!transactions.containsKey(transactionId)) {
            throw new RuntimeException("Transaction ID " + transactionId +" doesn't exist.");
        }
        TwoPCTransaction twoPCTransaction = transactions.get(transactionId);
        return twoPCTransaction.preCommit();
    }

    @Override
    public TwoPCState doCommit(int transactionId) {
        if(!transactions.containsKey(transactionId)) {
            throw new RuntimeException("Transaction ID " + transactionId +" doesn't exist.");
        }
        TwoPCTransaction twoPCTransaction = transactions.get(transactionId);
        return twoPCTransaction.doCommit();
    }

    @Override
    public TwoPCState abort(int transactionId) {
        if(!transactions.containsKey(transactionId)) {
            throw new RuntimeException("Transaction ID " + transactionId +" doesn't exist.");
        }
        TwoPCTransaction twoPCTransaction = transactions.get(transactionId);
        return twoPCTransaction.abort();
    }

    @Override
    public void process(int transactionId) {
        // должна быть сложная логика по оркестрации трехфазного commit.
        TwoPCState transactionState = canCommit(transactionId);
        log.info("Transaction #"+transactionId+". Phase canCommit. State: "+transactionState);
        if(transactionState == TwoPCState.WORKING) {
            transactionState = preCommit(transactionId);
            log.info("Transaction #"+transactionId+". Phase preCommit. State: "+transactionState);
            if(transactionState == TwoPCState.PREPARED) {
                transactionState = doCommit(transactionId);
                log.info("Transaction #"+transactionId+". Phase doCommit. State: "+transactionState);
            }
        }
    }
}
