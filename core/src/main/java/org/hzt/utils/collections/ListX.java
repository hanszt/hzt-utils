package org.hzt.utils.collections;

import org.hzt.utils.PreConditions;
import org.hzt.utils.Transformable;
import org.hzt.utils.iterables.Reversable;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static org.hzt.utils.PreConditions.require;

/**
 * This class represents an immutable non-null list. When a list of this interface is created, all null values are filtered out
 *
 * @param <E> the type of the elements
 * @author Hans Zuidervaart
 */
public interface ListX<E> extends CollectionX<E>,
        Transformable<ListX<E>>,
        BinarySearchable<ToIntFunction<E>>,
        Reversable<ListX<E>> {

    static <E> ListX<E> empty() {
        return new ImmutableListX<>();
    }

    static <E> ListX<E> of(final Iterable<E> iterable) {
        return new ImmutableListX<>(iterable);
    }

    @SafeVarargs
    static <E> ListX<E> of(final E... values) {
        return new ImmutableListX<>(values);
    }

    static <E> ListX<E> build(final Consumer<? super MutableListX<E>> mutableListConsumer) {
        final MutableListX<E> list = MutableListX.empty();
        mutableListConsumer.accept(list);
        return copyOf(list);
    }

    static <E> ListX<E> copyOf(final Collection<E> collection) {
        return new ImmutableListX<>(collection);
    }

    static <E> ListX<E> copyOfNullsAllowed(final List<E> list) {
        return new ImmutableListX<>(list);
    }

    default <R> R foldRight(final R initial, final BiFunction<E, R, R> operation) {
        final ListX<E> list = this;
        R accumulator = initial;
        if (list.isNotEmpty()) {
            final ListIterator<E> listIterator = list.listIterator(list.size());
            while (listIterator.hasPrevious()) {
                accumulator = operation.apply(listIterator.previous(), accumulator);
            }
        }
        return accumulator;
    }

    default <C extends Collection<E>> C takeLastTo(final IntFunction<C> collectionFactory, final int n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        final C emptyCollection = collectionFactory.apply(0);
        if (n == 0) {
            return emptyCollection;
        }
        if (n == 1) {
            emptyCollection.add(last());
            return emptyCollection;
        }
        final int size = size();
        if (n >= size) {
            return to(() -> collectionFactory.apply(size));
        }
        final C result = collectionFactory.apply(n);
        for (int index = size - n; index < size; index++) {
            result.add(get(index));
        }
        return result;
    }

    default ListX<E> takeLast(final int n) {
        return ListX.copyOfNullsAllowed(takeLastTo(MutableListX::withInitCapacity, n));
    }

    Optional<E> findRandom();

    @Override
    ListX<E> shuffled();

    default ListX<E> reversed() {
        return Sequence.reverseOf(this).toListX();
    }

    default E random() {
        return findRandom().orElseThrow(NoSuchElementException::new);
    }

    default Optional<E> findRandom(final Random random) {
        return isNotEmpty() ? Optional.of(get(random.nextInt(size()))) : Optional.empty();
    }

    default E random(final Random random) {
        return findRandom(random).orElseThrow(NoSuchElementException::new);
    }

    /**
     * @see BinarySearchable#binarySearch(int, int, Object)
     * @see java.util.Arrays#binarySearch(Object[], Object, Comparator)
     */
    default int binarySearch(final int fromIndex, final int toIndex, final ToIntFunction<E> comparison) {
        return BinarySearchable.binarySearch(size(), fromIndex, toIndex, mid -> comparison.applyAsInt(get(mid)));
    }

    default <T extends Comparable<? super T>> int binarySearchFor(final E valueToSearch) {
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
    default boolean containsNot(final E e) {
        return !contains(e);
    }

    E get(int index);

    int indexOf(Object o);

    int lastIndexOf(Object o);

    @Override
    default E first() {
        return get(0);
    }

    @Override
    default E last() {
        return get(lastIndex());
    }

    ListIterator<E> listIterator();

    ListIterator<E> listIterator(int index);

    ListX<E> headTo(int toIndex);

    ListX<E> tailFrom(int fromIndex);

    ListX<E> subList(int fromIndex, int toIndex);

    default <C extends Collection<E>> C skipLastTo(final IntFunction<C> collectionFactory, final int n) {
        require(n >= 0, () -> "Requested element count " + n + " is less than zero.");
        final int newSize = size() - n;
        return takeTo(() -> collectionFactory.apply(newSize), Math.max(newSize, 0));
    }

    default ListX<E> skipLast(final int n) {
        return ListX.copyOf(skipLastTo(MutableListX::withInitCapacity, n));
    }

    default ListX<E> skipLastWhile(final Predicate<? super E> predicate) {
        return ListX.copyOf(skipLastWhileTo(MutableListX::withInitCapacity, predicate));
    }

    default <C extends Collection<E>> C skipLastWhileTo(final IntFunction<C> collectionFactory,
                                                        final Predicate<? super E> predicate) {
        final C empty = collectionFactory.apply(0);
        if (isEmpty()) {
            return empty;
        }
        final ListIterator<E> iterator = listIterator(size());
        int counter = 0;
        while (iterator.hasPrevious()) {
            if (!predicate.test(iterator.previous())) {
                final int newSize = size() - counter;
                return takeTo(() -> collectionFactory.apply(newSize), iterator.nextIndex() + 1);
            }
            counter++;
        }
        return empty;
    }

    default ListX<E> takeLastWhile(final Predicate<? super E> predicate) {
        return ListX.copyOf(takeLastWhileTo(MutableListX::withInitCapacity, predicate));
    }

    default <C extends Collection<E>> C takeLastWhileTo(final IntFunction<C> collectionFactory,
                                                        final Predicate<? super E> predicate) {
        final C collection = collectionFactory.apply(0);
        if (isEmpty()) {
            return collection;
        }
        final int size = size();
        final ListIterator<E> iterator = listIterator(size);
        while (iterator.hasPrevious()) {
            if (!predicate.test(iterator.previous())) {
                iterator.next();
                final int expectedSize = size - iterator.nextIndex();
                if (expectedSize == 0) {
                    return collection;
                }
                final C result = collectionFactory.apply(expectedSize);
                while (iterator.hasNext()) {
                    result.add(iterator.next());
                }
                return result;
            }
        }
        return collection;
    }

    @Override
    default ListX<E> onEach(final Consumer<? super E> consumer) {
        return ListX.of(CollectionX.super.onEach(consumer));
    }

    @Override
    default <R> ListX<E> onEach(final Function<? super E, ? extends R> selector, final Consumer<? super R> consumer) {
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
