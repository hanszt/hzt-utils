package org.hzt.fx.utils.function;

@FunctionalInterface
public interface ToFloatFunction<T> {

    float applyAsFloat(T value);
}
