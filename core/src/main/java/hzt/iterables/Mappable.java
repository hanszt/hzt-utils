package hzt.iterables;

import hzt.collections.MutableListX;
import hzt.collections.MutableSetX;
import hzt.collections.SetX;
import hzt.strings.StringX;
import hzt.tuples.IndexedValue;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Mappable<T> extends IndexedIterable<T> {

    <R> Mappable<R> map(@NotNull Function<? super T, ? extends R> mapper);

    default <R, C extends Collection<R>> C mapTo(@NotNull Supplier<C> collectionFactory,
                                                 @NotNull Function<? super T, ? extends R> mapper) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, Objects::nonNull, mapper, It::noFilter);
    }

    <R> Mappable<R> mapNotNull(@NotNull Function<? super T, ? extends R> mapper);

    default <R, C extends Collection<R>> C mapNotNullTo(@NotNull Supplier<C> collectionFactory,
                                                        @NotNull Function<? super T, ? extends R> mapper) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, Objects::nonNull, mapper, Objects::nonNull);
    }

    <R> Mappable<R> mapIndexed(@NotNull BiFunction<Integer, ? super T, ? extends R> mapper);

    default <R, C extends Collection<R>> C mapIndexedTo(@NotNull Supplier<C> collectionFactory,
                                                        @NotNull BiFunction<Integer, ? super T, ? extends R> mapper) {
        return withIndex().mapTo(collectionFactory, indexedValue -> mapper.apply(indexedValue.index(), indexedValue.value()));
    }

    <R> Mappable<R> flatMap(@NotNull Function<T, Iterable<R>> mapper);

    default <R, I extends Iterable<R>, C extends Collection<R>> C flatMapTo(
            @NotNull Supplier<C> collectionSupplier,
            @NotNull Function<? super T, ? extends I> mapper) {
        final C collection = collectionSupplier.get();
        for (T t : this) {
            final I c = mapper.apply(t);
            if (c == null) {
                continue;
            }
            for (R r : c) {
                if (r != null) {
                    collection.add(r);
                }
            }
        }
        return collection;
    }
    <R> Mappable<R> mapMulti(@NotNull BiConsumer<? super T, ? super Consumer<R>> mapper);

    default <R, C extends Collection<R>> C mapMultiTo(
            @NotNull Supplier<C> collectionSupplier,
            @NotNull BiConsumer<? super T, ? super Consumer<R>> mapper) {
        C collection = collectionSupplier.get();
        for (T t : this) {
            mapper.accept(t, (Consumer<R>) collection::add);
        }
        return collection;
    }

    <R> Mappable<StringX> mapToStringX(@NotNull Function<? super T, ? extends R> function);

    @Override
    Mappable<IndexedValue<T>> withIndex();

    default <R> List<R> toListOf(@NotNull Function<? super T, ? extends R> transform) {
        return List.copyOf(mapNotNullTo(MutableListX::empty, transform));
    }

    default <R> SetX<R> toSetXOf(@NotNull Function<? super T, ? extends R> transform) {
        return mapNotNullTo(MutableSetX::empty, transform);
    }

    default <R> Set<R> toSetOf(@NotNull Function<? super T, ? extends R> transform) {
        return Collections.unmodifiableSet(this.<R, MutableSetX<R>>mapNotNullTo(MutableSetX::empty, transform));
    }
}
