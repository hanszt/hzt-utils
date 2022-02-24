package hzt.iterables;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Filterable<T> extends IndexedIterable<T> {

    Filterable<T> filter(@NotNull Predicate<T> predicate);

    <R> Filterable<T> filterBy(@NotNull Function<? super T, ? extends R> selector, @NotNull Predicate<R> predicate);

    Filterable<T> filterNot(@NotNull Predicate<T> predicate);

    Filterable<T> filterIndexed(@NotNull BiPredicate<Integer, T> predicate);
}
