package org.hzt.utils.iterables;

import org.hzt.utils.strings.StringX;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Mappable<T> extends IndexedIterable<T> {

    <R> Mappable<R> map(@NotNull Function<? super T, ? extends R> mapper);

    <R> Mappable<R> mapNotNull(@NotNull Function<? super T, ? extends R> mapper);

    <R> Mappable<R> mapIndexed(@NotNull BiFunction<Integer, ? super T, ? extends R> mapper);

    <R> Mappable<R> flatMap(@NotNull Function<? super T, ? extends Iterable<? extends R>> mapper);

    <R> Mappable<R> mapMulti(@NotNull BiConsumer<? super T, ? super Consumer<R>> mapper);

    <R> Mappable<StringX> mapToStringX(@NotNull Function<? super T, ? extends R> function);
}
