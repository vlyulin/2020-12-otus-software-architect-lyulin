package ru.otus.spring.app.twopc.common.models;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TwoPCTransactionImpl implements TwoPCTransaction {

    private int transactionId;
    private TwoPCState transactionState;
    private TwoPCState requiredTransactionState;
    private Set<TwoPCResource> resources = new HashSet<>();

    @Override
    public void add(TwoPCResource resource) {
        resources.add(resource);
    }

    @Override
    public void add(Set<TwoPCResource> resources) {
        resources.addAll(resources);
    }

    @Override
    public TwoPCState getState() {
        return transactionState;
    }

    @Override
    public TwoPCState getRequiredState() {
        return requiredTransactionState;
    }

    @Override
    public TwoPCState canCommit() {
        requiredTransactionState = TwoPCState.WORKING;
        boolean allCanCommit = true;
        for(TwoPCResource res: resources) {
                if (res.canCommit(getTransactionId()) != TwoPCState.WORKING) {
                    allCanCommit = false;
                }
        }
        if(allCanCommit) { this.transactionState = TwoPCState.WORKING; }
        return transactionState;
    }

    @Override
    public TwoPCState preCommit() {
        requiredTransactionState = TwoPCState.PREPARED;
        boolean allPrepared = true;
        for(TwoPCResource res: resources) {
            if(res.getState() == TwoPCState.WORKING) {
                res.preCommit(getTransactionId());
            }
            if(res.getState().isLessThan(TwoPCState.WORKING)) {
                allPrepared = false;
            }
        }
        if(allPrepared) { this.transactionState = TwoPCState.PREPARED; }
        return transactionState;
    }

    @Override
    public TwoPCState doCommit() {
        requiredTransactionState = TwoPCState.COMMITTED;
        boolean allCommited = true;
        for(TwoPCResource res: resources) {
            if(res.getState() == TwoPCState.PREPARED) {
                res.doCommit(getTransactionId());
            }
            if(res.getState().isLessThan(TwoPCState.COMMITTED)) {
                allCommited = false;
            }
        }
        if(allCommited) { this.transactionState = TwoPCState.COMMITTED; }
        return transactionState;
    }

    @Override
    public TwoPCState abort() {
        requiredTransactionState = TwoPCState.ABORTED;
        boolean allAborted = true;
        for(TwoPCResource res: resources) {
            if(res.getState() != TwoPCState.ABORTED) {
                res.abort(getTransactionId());
            }
            if(res.getState() != TwoPCState.ABORTED) {
                allAborted = false;
            }
        }
        if(allAborted) { this.transactionState = TwoPCState.ABORTED; }
        return transactionState;
    }
}
