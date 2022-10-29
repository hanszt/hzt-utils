package org.hzt.fx.utils;

import org.hzt.fx.utils.function.FloatConsumer;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public interface FloatIterator extends PrimitiveIterator<Float, FloatConsumer> {

    float nextFloat();

    @Override
    default void forEachRemaining(FloatConsumer action) {
        while (hasNext()) {
            action.accept(nextFloat());
        }
    }

    @Override
    default Float next() {
        throw new NoSuchElementException( "Use next float instead");
    }
}
