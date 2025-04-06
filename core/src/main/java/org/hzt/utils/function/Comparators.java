package org.hzt.utils.function;

import org.hzt.utils.Objects;

import java.io.Serializable;
import java.util.Comparator;

public final class Comparators {

    private Comparators() {
    }

    public static <T, U> AbstractComparator<T> comparing(
            final Function<? super T, ? extends U> keyExtractor,
            final Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return new AbstractComparator<T>() {

            public int compare(final T c1, final T c2) {
                return keyComparator.compare(keyExtractor.apply(c1), keyExtractor.apply(c2));
            }
        };
    }

    public static <T, U extends Comparable<? super U>> AbstractComparator<T> comparing(
            final Function<? super T, ? extends U> keyExtractor
    ) {
        Objects.requireNonNull(keyExtractor);
        return new AbstractComparator<T>() {
            public int compare(final T c1, final T c2) {
                return keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
            }
        };
    }

    public abstract static class AbstractComparator<T> implements Comparator<T>, Serializable {

        public AbstractComparator<T> thenComparing(final Comparator<? super T> other) {
            Objects.requireNonNull(other);
            return new AbstractComparator<T>() {

                public int compare(final T c1, final T c2) {
                    int res = AbstractComparator.this.compare(c1, c2);
                    return (res != 0) ? res : other.compare(c1, c2);
                }
            };
        }
    }
}
