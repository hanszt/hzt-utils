package org.hzt.utils.function.primitives;

@FunctionalInterface
public interface IntBiFunction<T> {

    T apply(int value1, int value2);
}
