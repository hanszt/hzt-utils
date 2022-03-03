package org.hzt.utils.iterables;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Groupable<T> extends Iterable<T> {

    default MapX<T, MutableListX<T>> group() {
        return groupBy(It::self);
    }

    default <K> MapX<K, MutableListX<T>> groupBy(@NotNull Function<? super T, ? extends K> classifier) {
        return groupMapping(classifier, It::self);
    }

    default <K, R> MapX<K, MutableListX<R>> groupMapping(@NotNull Function<? super T, ? extends K> classifier,
                                                         @NotNull Function<? super T, ? extends R> valueMapper) {
        return IterableReductions.groupMapping(this, classifier, valueMapper);
    }

    default Pair<ListX<T>, ListX<T>> partition(@NotNull Predicate<T> predicate) {
        return partitionMapping(predicate, It::self);
    }

    default <R> Pair<ListX<R>, ListX<R>> partitionMapping(@NotNull Predicate<T> predicate,
                                                          @NotNull Function<? super T, ? extends R> resultMapper) {
        return IterableReductions.partitionMapping(this, predicate, resultMapper);
    }
}
