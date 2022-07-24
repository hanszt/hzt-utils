package org.hzt.utils.iterables.primitives;

import org.hzt.utils.iterables.Streamable;

import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface IntStreamable extends PrimitiveIterable.OfInt, Streamable<Integer, IntStream> {
    @Override
    default IntStream stream() {
        return StreamSupport.intStream(spliterator(), false);
    }
}
