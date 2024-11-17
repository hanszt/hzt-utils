package org.hzt.utils;



import java.util.Objects;
import java.util.function.Supplier;

/**
 * Simple class to compute a value in a lazy fashion. Based on vavr
 * <p>
 * @see io.vavr.Lazy
 *
 * This <code>Lazy</code> is thread-safe.
 *
 * @param <T> The value to compute lazy
 */
@SuppressWarnings("JavadocReference")
public final class Lazy<T> implements Transformable<T> {

    private final Supplier<T> supplier;

    private volatile T value;

    private Lazy(final Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "Supplier must not be null");
        this.supplier = supplier;
    }

    public static <T> Lazy<T> of(final Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    public T get() {
        T v = this.value;
        if (v != null) {
            return v;
        }
        synchronized (supplier) {
            v = this.value;
            if (v == null) {
                v = this.value = supplier.get();
            }
            return Objects.requireNonNull(v);
        }
    }
}
