package hzt.io;

import java.util.function.Predicate;

public class Closer<T> implements AutoCloseable {

    private final T resource;
    private final ConsumingAutoClosable<T> closeable;

    public Closer(T resource, ConsumingAutoClosable<T> consumingAutoClosable) {
        this.resource = resource;
        this.closeable = consumingAutoClosable;
    }

    public static <T> Closer<T> forResource(T resource, ConsumingAutoClosable<T> consumingAutoClosable) {
        return new Closer<>(resource, consumingAutoClosable);
    }

    @Override
    public void close() {
        try {
            closeable.close(resource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public <R> R execute(ThrowingFunction<T, R> function) {
        try {
            return function.apply(resource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean test(Predicate<T> predicate) {
        return predicate.test(resource);
    }

    public T getResource() {
        return resource;
    }

    @FunctionalInterface
    public interface ConsumingAutoClosable<T> {

        @SuppressWarnings("all")
        void close(T t) throws Exception;
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R> {

        @SuppressWarnings("all")
        R apply(T t) throws Exception;
    }
}
