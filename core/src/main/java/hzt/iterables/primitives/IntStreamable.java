package hzt.iterables.primitives;

import hzt.iterables.Streamable;

import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface IntStreamable extends IntIterable, Streamable<IntStream> {
    @Override
    default IntStream stream() {
        return StreamSupport.intStream(spliterator(), false);
    }

    @Override
    default IntStream parallelStream() {
        return StreamSupport.intStream(spliterator(), true);
    }
}
