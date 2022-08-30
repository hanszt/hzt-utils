package org.hzt.utils.function.primitives;

@FunctionalInterface
public interface DoubleIndexedFunction {

    double applyAsDouble(int index, double value);
}
