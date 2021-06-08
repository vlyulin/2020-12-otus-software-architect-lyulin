package ru.otus.spring.app.twopc.common.models;

import ru.otus.spring.app.twopc.common.models.TwoPCAck;

import java.util.Set;

public interface TwoPCTransaction {
    void add(TwoPCResource resource);
    void add(Set<TwoPCResource> resources);
    int getTransactionId();
    TwoPCState canCommit();
    TwoPCState preCommit();
    TwoPCState doCommit();
    TwoPCState abort();
    TwoPCState getState();
    TwoPCState getRequiredState();
}
