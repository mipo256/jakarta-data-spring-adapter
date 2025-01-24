package io.mpolivaha.jdsa.utils;

import java.util.function.Supplier;

public class Lazy<T> {

    private boolean resolved;
    private T value;
    private final Supplier<T> supplier;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
        this.resolved = false;
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    public T get() {
        if (resolved) {
            return value;
        } else {
            synchronized (this) {
                if (resolved) {
                    return value;
                }
                resolved = true;
                value = supplier.get();
                return value;
            }
        }
    }
}
