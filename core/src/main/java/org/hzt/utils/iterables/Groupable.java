package org.hzt.utils.iterables;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.tuples.Pair;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Groupable<T> extends Iterable<T> {

    default MapX<T, MutableListX<T>> group() {
        return groupBy(It::self);
    }

    default <K> MapX<K, MutableListX<T>> groupBy(final Function<? super T, ? extends K> classifier) {
        return groupMapping(classifier, It::self);
    }

    default <K, R> MapX<K, MutableListX<R>> groupMapping(final Function<? super T, ? extends K> classifier,
                                                         final Function<? super T, ? extends R> valueMapper) {
        return IterableReductions.groupMapping(this, classifier, valueMapper);
    }

    default <K> Grouping<T, K> groupingBy(final Function<? super T, ? extends K> classifier) {
        return new Grouping<>() {
            @Override
            public K keyOf(final T element) {
                return classifier.apply(element);
            }

            @Override
            public Iterator<T> iterator() {
                return Groupable.this.iterator();
            }
        };
    }

    default Pair<ListX<T>, ListX<T>> partition(final Predicate<T> predicate) {
        return partitionMapping(predicate, It::self);
    }

    default <R> Pair<ListX<R>, ListX<R>> partitionMapping(final Predicate<T> predicate,
                                                          final Function<? super T, ? extends R> resultMapper) {
        return IterableReductions.partitionMapping(this, predicate, resultMapper);
    }
}
