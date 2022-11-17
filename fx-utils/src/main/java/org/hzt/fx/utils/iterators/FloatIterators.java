package org.hzt.fx.utils.iterators;

import org.hzt.fx.utils.function.FloatPredicate;
import org.hzt.fx.utils.function.FloatSupplier;
import org.hzt.fx.utils.function.FloatUnaryOperator;

public final class FloatIterators {

    private FloatIterators() {
    }

    public static FloatIterator arrayIterator(float... floats) {
        return new FloatIterator() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < floats.length;
            }

            @Override
            public float nextFloat() {
                return floats[index++];
            }
        };
    }

    public static FloatIterator mappingIterator(FloatIterator iterator, FloatUnaryOperator operator) {
        return new FloatIterator() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public float nextFloat() {
                return operator.applyAsFloat(iterator.nextFloat());
            }
        };
    }

    public static FloatIterator filteringIterator(FloatIterator iterator, FloatPredicate predicate) {
        return new FloatFilteringIterator(iterator, predicate, true);
    }

    public static FloatIterator generatorIterator(FloatSupplier initValueSupplier, FloatUnaryOperator nextValueSupplier) {
        return new FloatGeneratorIterator(initValueSupplier, nextValueSupplier);
    }
}
