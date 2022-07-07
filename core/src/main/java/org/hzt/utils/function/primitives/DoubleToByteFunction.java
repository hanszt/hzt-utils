package org.hzt.utils.function.primitives;

@FunctionalInterface
public interface DoubleToByteFunction {

    byte applyAsByte(double value);
}
