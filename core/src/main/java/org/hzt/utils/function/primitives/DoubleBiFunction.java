package org.hzt.utils.function.primitives;

@FunctionalInterface
public interface DoubleBiFunction<T> {

    T apply(double value1, double value2);
}
