package ru.otus.spring.app.twopc.common.models;

public enum TwoPCState {
    ABORTED(0),
    WORKING(1),
    PREPARED(2),
    COMMITTED(3);

    private Integer pos;

    TwoPCState(int pos) {
        this.pos = pos;
    }

    public boolean isLessThan(TwoPCState other) {
        return this.pos < other.pos;
    }
}
