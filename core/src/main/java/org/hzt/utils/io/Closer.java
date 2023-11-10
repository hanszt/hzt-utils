package org.hzt.utils.io;

import org.hzt.utils.function.ThrowingConsumer;
import org.hzt.utils.function.ThrowingFunction;

import java.util.function.Predicate;

public final class Closer<T> implements AutoCloseable {

    private final T resource;
    private final ThrowingConsumer<? super T> closeable;

    private Closer(T resource, ThrowingConsumer<? super T> consumingAutoClosable) {
        this.resource = resource;
        this.closeable = consumingAutoClosable;
    }

    public static <T> Closer<T> forResource(T resource,
                                            ThrowingConsumer<? super T> consumingAutoClosable) {
        return new Closer<>(resource, consumingAutoClosable);
    }

    @Override
    public void close() {
        try {
            closeable.accept(resource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public <R> R apply(ThrowingFunction<? super T, ? extends R> function) {
        try {
            return function.apply(resource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public <R> R applyAndClose(ThrowingFunction<? super T, ? extends R> function) {
        try {
            return apply(function);
        } finally {
            close();
        }
    }

    public void execute(ThrowingConsumer<? super T> consumer) {
        try {
            consumer.accept(resource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void executeAndClose(ThrowingConsumer<? super T> consumer) {
        try {
            execute(consumer);
        } finally {
            close();
        }
    }

    public boolean test(Predicate<? super T> predicate) {
        return predicate.test(resource);
    }

    public boolean testAndClose(Predicate<? super T> predicate) {
        try {
            return predicate.test(resource);
        } finally {
            close();
        }
    }

    public T getResource() {
        return resource;
    }
}
