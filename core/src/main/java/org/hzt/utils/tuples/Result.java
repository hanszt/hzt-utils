package org.hzt.utils.tuples;

import org.hzt.utils.function.ThrowingFunction;

import java.util.Optional;
import java.util.function.Function;

public final class Result<T, R> {

    private final Throwable throwable;
    private final T originalValue;
    private final R resultVal;

    public Result(T value, R resultValue) {
        this.originalValue = value;
        this.resultVal = resultValue;
        this.throwable = null;
    }

    public Result(T value, Throwable throwable) {
        this.originalValue = value;
        this.resultVal = null;
        this.throwable = throwable;
    }

    public static <T, R> Function<T, Result<T, R>> catching(ThrowingFunction<? super T, ? extends R> throwingFunction) {
        return t -> {
            try {
                final R result = throwingFunction.apply(t);
                return new Result<>(t, result);
            } catch (Exception e) {
                return new Result<>(t, e);
            }
        };
    }

    public Optional<T> originalValue() {
        return Optional.ofNullable(originalValue);
    }

    public Optional<R> succes() {
        return Optional.ofNullable(resultVal);
    }

    public Optional<Throwable> error() {
        return Optional.ofNullable(throwable);
    }

    public boolean hasError() {
        return throwable != null;
    }

    @Override
    public String toString() {
        return "Result{" +
                "throwable=" + throwable +
                ", originalValue=" + originalValue +
                ", resultVal=" + resultVal +
                '}';
    }
}
