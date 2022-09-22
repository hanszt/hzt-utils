package org.hzt.utils.iterators.primitives;

import java.util.ListIterator;
import java.util.PrimitiveIterator;

@SuppressWarnings({"squid:S2972", "squid:S2272"})
public interface PrimitiveListIterator<T> extends ListIterator<T> {

    interface OfInt extends PrimitiveIterator.OfInt, PrimitiveListIterator<Integer> {

        int previousInt();

        @Override
        default Integer previous() {
            throw new UnsupportedOperationException("previousInt() must be called instead");
        }

        @Override
        default Integer next() {
            return PrimitiveIterator.OfInt.super.next();
        }

        @Override
        int nextIndex();

        @Override
        int previousIndex();

        @Override
        default void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        default void set(Integer integer) {
            throw new UnsupportedOperationException();
        }

        @Override
        default void add(Integer integer) {
            throw new UnsupportedOperationException();
        }
    }

    interface OfLong extends PrimitiveIterator.OfLong, PrimitiveListIterator<Long> {

        long previousLong();

        @Override
        default Long previous() {
            throw new UnsupportedOperationException("previousLong() must be called instead");
        }

        @Override
        default Long next() {
            return PrimitiveIterator.OfLong.super.next();
        }

        @Override
        int nextIndex();

        @Override
        int previousIndex();

        @Override
        default void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        default void set(Long l) {
            throw new UnsupportedOperationException();
        }

        @Override
        default void add(Long l) {
            throw new UnsupportedOperationException();
        }
    }

    interface OfDouble extends PrimitiveIterator.OfDouble, PrimitiveListIterator<Double> {

        double previousDouble();

        @Override
        default Double previous() {
            throw new UnsupportedOperationException("previousDouble() must be called instead");
        }

        @Override
        default Double next() {
            return PrimitiveIterator.OfDouble.super.next();
        }

        @Override
        int nextIndex();

        @Override
        int previousIndex();

        @Override
        default void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        default void set(Double d) {
            throw new UnsupportedOperationException();
        }

        @Override
        default void add(Double d) {
            throw new UnsupportedOperationException();
        }
    }
}
