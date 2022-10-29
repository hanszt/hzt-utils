package org.hzt.utils.function;

@FunctionalInterface
public interface ThrowingFunction<T, R> {

    @SuppressWarnings("squid:S112")
    R apply(T t) throws Exception;
}
