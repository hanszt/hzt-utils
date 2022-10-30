package org.hzt.utils.sequences;

import org.hzt.utils.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class SequenceHelper {

    private SequenceHelper() {
    }

    static <T, R> @NotNull Iterator<R> transformingIterator(Iterator<T> iterator, Function<? super T, ? extends R> mapper) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public R next() {
                return mapper.apply(iterator.next());
            }
        };
    }

    public static void checkInitWindowSizeAndStep(int size, int step) {
        PreConditions.require(size > 0 && step > 0, () -> getErrorMessage(size, step));

    }

    private static String getErrorMessage(int size, int step) {
        if (size != step) {
            return "Both size " + size + " and step " + step + " must be greater than zero.";
        }
        return "size " + size + " must be greater than zero.";
    }

    public static <T> Iterator<T> interspersingIterator(Iterator<T> iterator, UnaryOperator<T> operator) {
        return new Iterator<>() {
            private T current = null;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                if (current != null) {
                    final var valueToInsert = operator.apply(current);
                    current = null;
                    return valueToInsert;
                } else {
                    current = iterator.next();
                    return current;
                }
            }
        };
    }

    public static <T> Iterator<T> interspersingIterator(Iterator<T> iterator,
                                                        Supplier<T> initValSupplier,
                                                        UnaryOperator<T> operator) {
        return new Iterator<>() {
            private T valueToInsert = null;
            private boolean insertValue = false;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                if (insertValue) {
                    insertValue = false;
                    valueToInsert = valueToInsert == null ? initValSupplier.get() : operator.apply(this.valueToInsert);
                    return valueToInsert;
                } else {
                    insertValue = true;
                    return iterator.next();
                }
            }
        };
    }
}
