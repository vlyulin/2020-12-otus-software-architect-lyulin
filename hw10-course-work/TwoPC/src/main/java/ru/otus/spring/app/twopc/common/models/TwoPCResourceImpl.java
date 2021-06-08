package ru.otus.spring.app.twopc.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Consumer;

@Data
@AllArgsConstructor
public class TwoPCResourceImpl<T> {

    private T t;
    private Consumer<T> biFunction;

    public void doCommit() {
        biFunction.accept(t);
    }
}
