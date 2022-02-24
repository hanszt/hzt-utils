package hzt.iterables.primitives;

import hzt.iterables.Streamable;

import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface DoubleStreamable extends DoubleIterable, Streamable<DoubleStream> {
    @Override
    default DoubleStream stream() {
        return StreamSupport.doubleStream(spliterator(), false);
    }

    @Override
    default DoubleStream parallelStream() {
        return StreamSupport.doubleStream(spliterator(), true);
    }
}
