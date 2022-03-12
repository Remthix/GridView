package com.sdremthix.com.gridview.domain;

public class Pair<T, E> {
    private final T first;
    private final E second;

    public Pair(T first, E second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Getters
     */
    public T getFirst() {
        return first;
    }

    public E getSecond() {
        return second;
    }
}
