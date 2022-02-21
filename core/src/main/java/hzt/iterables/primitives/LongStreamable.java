package hzt.iterables.primitives;

import hzt.iterables.Streamable;

import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface LongStreamable extends LongIterable, Streamable<LongStream> {
    @Override
    default LongStream stream() {
        return StreamSupport.longStream(spliterator(), false);
    }

    @Override
    default LongStream parallelStream() {
        return StreamSupport.longStream(spliterator(), true);
    }
}
