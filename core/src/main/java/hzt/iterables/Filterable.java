package hzt.iterables;

import hzt.tuples.IndexedValue;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Filterable<T> extends IndexedIterable<T> {

    Filterable<T> filter(@NotNull Predicate<T> predicate);

    default <C extends Collection<T>> C filterTo(@NotNull Supplier<C> collectionFactory,
                                                 @NotNull Predicate<T> predicate) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, predicate, It::self, It::noFilter);
    }

    <R> Filterable<T> filterBy(@NotNull Function<? super T, ? extends R> selector, @NotNull Predicate<R> predicate);

    Filterable<T> filterNot(@NotNull Predicate<T> predicate);

    default <C extends Collection<T>> C filterNotTo(@NotNull Supplier<C> collectionFactory,
                                                    @NotNull Predicate<T> predicate) {
        return filterTo(collectionFactory, predicate.negate());
    }

    Filterable<T> filterIndexed(@NotNull BiPredicate<Integer, T> predicate);

    default <C extends Collection<T>> C filterIndexedTo(@NotNull Supplier<C> collectionFactory,
                                                        @NotNull BiPredicate<Integer, T> predicate) {
        C collection = collectionFactory.get();
        for (IndexedValue<T> indexedValue : withIndex()) {
            final T value = indexedValue.value();
            if (predicate.test(indexedValue.index(), value)) {
                collection.add(value);
            }
        }
        return collection;
    }
}
