package org.hzt.utils;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Simple class to compute a value in a lazy fashion. Based on vavr
 * <p>
 * @see io.vavr.Lazy
 *
 * @param <T> The value to compute lazy
 */
@SuppressWarnings("JavadocReference")
public final class Lazy<T> {

    private Supplier<T> supplier;

    private T value;

    private Lazy(Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "Supplier must not be null");
        this.supplier = supplier;
    }


    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    public T get() {
        value = supplier != null ? supplier.get() : value;
        supplier = null;
        return value;
    }
}
