package org.hzt.fx.utils.iterators;

import org.hzt.fx.utils.function.FloatConsumer;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public interface FloatIterator extends PrimitiveIterator<Float, FloatConsumer> {

    @Override
    default void forEachRemaining(final FloatConsumer action) {
        while (hasNext()) {
            action.accept(nextFloat());
        }
    }

    float nextFloat();

    @Override
    default Float next() {
        throw new NoSuchElementException( "Use next float instead");
    }
}
