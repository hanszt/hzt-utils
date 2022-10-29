package org.hzt.utils.collections;

import org.hzt.utils.PreConditions;
import org.hzt.utils.Transformable;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Random;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static org.hzt.utils.PreConditions.require;

/**
 * This class represents an immutable non-null list. When a list of this interface is created, all null values are filtered out
 *
 * @param <E> the type of the elements
 * @author Hans Zuidervaart
 */
public interface ListX<E> extends CollectionX<E>, Transformable<ListX<E>>, BinarySearchable<ToIntFunction<E>> {

    static <E> ListX<E> empty() {
        return new ImmutableListX<>();
    }

    static <E> ListX<E> of(Iterable<E> iterable) {
        return new ImmutableListX<>(iterable);
    }

    @SafeVarargs
    static <E> ListX<E> of(E... values) {
        return new ImmutableListX<>(values);
    }

    static <E> ListX<E> build(Consumer<? super MutableListX<E>> mutableListConsumer) {
        MutableListX<E> list = MutableListX.empty();
        mutableListConsumer.accept(list);
        return copyOf(list);
    }

    static <E> ListX<E> copyOf(Collection<E> collection) {
        return new ImmutableListX<>(collection);
    }

    static <E> ListX<E> copyOfNullsAllowed(List<E> list) {
        return new ImmutableListX<>(list);
    }

    default <R> R foldRight(@NotNull R initial, @NotNull BiFunction<E, R, R> operation) {
        ListX<E> list = this;
        R accumulator = initial;
        if (list.isNotEmpty()) {
            final ListIterator<E> listIterator = list.listIterator(list.size());
            while (listIterator.hasPrevious()) {
                accumulator = operation.apply(listIterator.previous(), accumulator);
            }
        }
        return accumulator;
    }

    default <C extends Collection<E>> C takeLastTo(IntFunction<C> collectionFactory, int n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        final var emptyCollection = collectionFactory.apply(0);
        if (n == 0) {
            return emptyCollection;
        }
        C collection = Sequence.of(this).to(() -> emptyCollection);
        int size = collection.size();
        if (n >= size) {
            return collection;
        }
        if (n == 1) {
            return Sequence.of(last()).to(() -> emptyCollection);
        }
        C result = collectionFactory.apply(n);
        for (int index = size - n; index < size; index++) {
            result.add(get(index));
        }
        return result;
    }

    default ListX<E> takeLast(int n) {
        return ListX.copyOfNullsAllowed(takeLastTo(MutableListX::withInitCapacity, n));
    }

    Optional<E> findRandom();

    ListX<E> shuffled();

    default E random() {
        return findRandom().orElseThrow();
    }

    default Optional<E> findRandom(Random random) {
        return isNotEmpty() ? Optional.of(get(random.nextInt(size()))) : Optional.empty();
    }

    default E random(Random random) {
        return findRandom(random).orElseThrow();
    }

    /**
     * @see BinarySearchable#binarySearch(int, int, Object)
     * @see java.util.Arrays#binarySearch(Object[], Object, Comparator)
     */
    default int binarySearch(int fromIndex, int toIndex, ToIntFunction<E> comparison) {
        return BinarySearchable.binarySearch(size(), fromIndex, toIndex, mid -> comparison.applyAsInt(get(mid)));
    }

    default <T extends Comparable<? super T>> int binarySearchFor(E valueToSearch) {
        if (valueToSearch instanceof Comparable<?>) {
            //noinspection unchecked
            return binarySearch(0, size(), (E e) -> ((T) e).compareTo((T) valueToSearch));
        }
        throw new IllegalStateException("Can not perform binary search by non comparable search value type: " +
                valueToSearch.getClass().getSimpleName());
    }

    @Override
    int size();

    default int lastIndex() {
        return size() - 1;
    }

    @Override
    boolean isEmpty();

    @Override
    default boolean isNotEmpty() {
        return !isEmpty();
    }

    @Override
    boolean contains(Object value);

    @Override
    default boolean containsNot(E e) {
        return !contains(e);
    }

    E get(int index);

    int indexOf(Object o);

    int lastIndexOf(Object o);

    @Override
    @NotNull
    default E first() {
        return get(0);
    }

    @Override
    @NotNull
    default E last() {
        return get(lastIndex());
    }

    ListIterator<E> listIterator();

    ListIterator<E> listIterator(int index);

    ListX<E> headTo(int toIndex);

    ListX<E> tailFrom(int fromIndex);

    ListX<E> subList(int fromIndex, int toIndex);

    default <C extends Collection<E>> C skipLastTo(Supplier<C> collectionFactory, int n) {
        require(n >= 0, () -> "Requested element count " + n + " is less than zero.");
        return takeTo(collectionFactory, Math.max((size() - n), 0));
    }

    default ListX<E> skipLast(int n) {
        return ListX.copyOf(takeTo(MutableListX::empty, Math.max((size() - n), 0)));
    }

    default ListX<E> skipLastWhile(@NotNull Predicate<? super E> predicate) {
        if (isEmpty()) {
            return ListX.empty();
        }
        ListIterator<E> iterator = listIterator(size());
        while (iterator.hasPrevious()) {
            if (!predicate.test(iterator.previous())) {
                return take(iterator.nextIndex() + 1L);
            }
        }
        return ListX.empty();
    }

    default ListX<E> takeLastWhile(@NotNull Predicate<? super E> predicate) {
        return ListX.copyOf(takeLastWhileTo(MutableListX::withInitCapacity, predicate));
    }

    default <C extends Collection<E>> C takeLastWhileTo(@NotNull IntFunction<C> collectionFactory,
                                                        @NotNull Predicate<? super E> predicate) {
        C collection = collectionFactory.apply(0);
        if (isEmpty()) {
            return collection;
        }
        ListIterator<E> iterator = listIterator(size());
        while (iterator.hasPrevious()) {
            if (!predicate.test(iterator.previous())) {
                iterator.next();
                int expectedSize = size() - iterator.nextIndex();
                if (expectedSize == 0) {
                    return collection;
                }
                C result = collectionFactory.apply(expectedSize);
                while (iterator.hasNext()) {
                    result.add(iterator.next());
                }
                return result;
            }
        }
        return Sequence.of(this).to(() -> collection);
    }

    @Override
    default @NotNull ListX<E> onEach(@NotNull Consumer<? super E> consumer) {
        return ListX.of(CollectionX.super.onEach(consumer));
    }

    @Override
    @NotNull
    default <R> ListX<E> onEach(@NotNull Function<? super E, ? extends R> selector, @NotNull Consumer<? super R> consumer) {
        return ListX.of(CollectionX.super.onEach(selector, consumer));
    }

    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    default IntRange indices() {
        return IntRange.of(0, size());
    }
}
