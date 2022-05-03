package com.sdremthix.com.gridview.domain.entities;

/**
 * Implementation for the simple pair object of two elements of the same or different types.
 *
 * @param <T> The generic type of the first element in the pair.
 * @param <E> The generic type of the second element in the pair.
 */
public final class Pair<T, E> {
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
