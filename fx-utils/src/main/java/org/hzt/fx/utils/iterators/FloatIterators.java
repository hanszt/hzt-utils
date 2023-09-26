package org.hzt.fx.utils.iterators;

import org.hzt.fx.utils.function.FloatPredicate;
import org.hzt.fx.utils.function.FloatSupplier;
import org.hzt.fx.utils.function.FloatUnaryOperator;

public final class FloatIterators {

    private FloatIterators() {
    }

    public static FloatIterator arrayIterator(final float... floats) {
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

    public static FloatIterator mappingIterator(final FloatIterator iterator, final FloatUnaryOperator operator) {
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

    public static FloatIterator filteringIterator(final FloatIterator iterator, final FloatPredicate predicate) {
        return new FloatFilteringIterator(iterator, predicate, true);
    }

    public static FloatIterator generatorIterator(final FloatSupplier initValueSupplier, final FloatUnaryOperator nextValueSupplier) {
        return new FloatGeneratorIterator(initValueSupplier, nextValueSupplier);
    }
}
