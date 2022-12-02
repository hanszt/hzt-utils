package org.hzt.fx.utils.sequences;

import javafx.collections.FXCollections;
import javafx.collections.ObservableFloatArray;
import org.hzt.fx.utils.collections.FloatArrayList;
import org.hzt.fx.utils.function.FloatPredicate;
import org.hzt.fx.utils.function.FloatUnaryOperator;
import org.hzt.fx.utils.iterables.FloatIterable;
import org.hzt.fx.utils.iterators.FloatIterator;
import org.hzt.fx.utils.iterators.FloatIterators;

import java.util.NoSuchElementException;

@FunctionalInterface
public interface FloatSequence extends FloatIterable {

    static FloatSequence of(float... floats) {
        return () -> FloatIterators.arrayIterator(floats);
    }

    static FloatSequence of(FloatIterable floatIterable) {
        return floatIterable::iterator;
    }

    static FloatSequence iterate(float seed, FloatUnaryOperator nextValueSupplier) {
        return () -> FloatIterators.generatorIterator(() -> seed, nextValueSupplier);
    }

    FloatIterator iterator();

    default FloatSequence map(FloatUnaryOperator operator) {
        return () -> FloatIterators.mappingIterator(iterator(), operator);
    }

    default FloatSequence filter(FloatPredicate predicate) {
        return () -> FloatIterators.filteringIterator(iterator(), predicate);
    }

    default float first() {
        final var iterator = iterator();
        if (iterator.hasNext()) {
            return iterator.nextFloat();
        }
        throw new NoSuchElementException();
    }

    default FloatArrayList toList() {
        final var floats = new FloatArrayList();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            floats.add(iterator.nextFloat());
        }
        return floats;
    }

    default float[] toArray() {
        return toList().toArray();
    }

    default ObservableFloatArray toObservableArray() {
        return FXCollections.observableFloatArray(toArray());
    }
}
