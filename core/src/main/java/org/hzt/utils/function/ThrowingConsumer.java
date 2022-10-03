package org.hzt.utils.function;

@FunctionalInterface
public interface ThrowingConsumer<T> {

    @SuppressWarnings("squid:S112")
    void accept(T t) throws Exception;
}
