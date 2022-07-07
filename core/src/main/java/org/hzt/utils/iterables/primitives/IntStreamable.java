package org.hzt.utils.iterables.primitives;

import org.hzt.utils.iterables.Streamable;

import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface IntStreamable extends PrimitiveIterable.OfInt, Streamable<IntStream> {
    @Override
    default IntStream stream() {
        return StreamSupport.intStream(spliterator(), false);
    }

    @Override
    default IntStream parallelStream() {
        return StreamSupport.intStream(spliterator(), true);
    }
}
