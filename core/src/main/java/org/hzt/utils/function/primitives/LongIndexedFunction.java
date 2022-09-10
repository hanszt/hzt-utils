package org.hzt.utils.function.primitives;

@FunctionalInterface
public interface LongIndexedFunction {

    long applyAsLong(int index, long value);
}
