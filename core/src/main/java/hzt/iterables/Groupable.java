package hzt.iterables;

import hzt.collections.ListView;
import hzt.collections.MapView;
import hzt.collections.MutableList;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Groupable<T> extends Iterable<T> {

    default MapView<T, MutableList<T>> group() {
        return groupBy(It::self);
    }

    default <K> MapView<K, MutableList<T>> groupBy(@NotNull Function<? super T, ? extends K> classifier) {
        return groupMapping(classifier, It::self);
    }

    default <K, R> MapView<K, MutableList<R>> groupMapping(@NotNull Function<? super T, ? extends K> classifier,
                                                           @NotNull Function<? super T, ? extends R> valueMapper) {
        return IterableReductions.groupMapping(this, classifier, valueMapper);
    }

    default Pair<ListView<T>, ListView<T>> partition(@NotNull Predicate<T> predicate) {
        return partitionMapping(predicate, It::self);
    }

    default <R> Pair<ListView<R>, ListView<R>> partitionMapping(@NotNull Predicate<T> predicate,
                                                                @NotNull Function<? super T, ? extends R> resultMapper) {
        return IterableReductions.partitionMapping(this, predicate, resultMapper);
    }
}
