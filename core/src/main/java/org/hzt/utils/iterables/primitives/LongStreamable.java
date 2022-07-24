package org.hzt.utils.iterables.primitives;

import org.hzt.utils.iterables.Streamable;

import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface LongStreamable extends PrimitiveIterable.OfLong, Streamable<Long, LongStream> {
    @Override
    default LongStream stream() {
        return StreamSupport.longStream(spliterator(), false);
    }
}
