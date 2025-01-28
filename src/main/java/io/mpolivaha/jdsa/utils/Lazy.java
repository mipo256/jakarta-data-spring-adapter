package io.mpolivaha.jdsa.utils;

import java.util.function.Supplier;

/**
 * The lazy wrapper around the value. The idea is that if the {@link #value} is
 * already resolved, the underlying {@link #supplier} is not going to be called again
 * to obtain the {@link #value}.
 *
 * @author Mikhail Polivakha
 */
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
