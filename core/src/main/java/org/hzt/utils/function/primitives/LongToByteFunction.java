package org.hzt.utils.function.primitives;

@FunctionalInterface
public interface LongToByteFunction {

    byte applyAsByte(long value);
}
