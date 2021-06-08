package ru.otus.spring.app.twopc.common.models;

import lombok.Data;

@Data
public class TwoPCAck {
    private TwoPCState state;

    public TwoPCAck(TwoPCState state) {
        this.state = state;
    }
}
