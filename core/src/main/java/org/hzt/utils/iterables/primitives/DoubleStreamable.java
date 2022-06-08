package org.hzt.utils.iterables.primitives;

import org.hzt.utils.iterables.Streamable;

import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface DoubleStreamable extends PrimitiveIterable.OfDouble, Streamable<DoubleStream> {
    @Override
    default DoubleStream stream() {
        return StreamSupport.doubleStream(spliterator(), false);
    }

    @Override
    default DoubleStream parallelStream() {
        return StreamSupport.doubleStream(spliterator(), true);
    }
}
