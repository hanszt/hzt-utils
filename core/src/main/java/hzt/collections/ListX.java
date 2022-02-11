package hzt.collections;

import hzt.PreConditions;
import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static hzt.PreConditions.require;

/**
 * This class represents an immutable non-null list. When a list of this interface is created, all null values are filtered out
 *
 * @param <E> the type of the elements
 * @author Hans Zuidervaart
 */
public interface ListX<E> extends CollectionView<E>, Transformable<ListX<E>> {

    static <E> ListX<E> empty() {
        return new ArrayListX<>();
    }

    static <E> ListX<E> of(Iterable<E> iterable) {
        return new ArrayListX<>(iterable);
    }

    static <E> ListX<E> of(Collection<E> iterable) {
        return new ArrayListX<>(iterable);
    }

    static <E> ListX<E> of(List<E> list) {
        return new ArrayListX<>(list);
    }

    @SafeVarargs
    static <E> ListX<E> of(E... values) {
        return new ArrayListX<>(values);
    }

    static ListX<Boolean> ofBools(boolean... values) {
        MutableListX<Boolean> valueList = MutableListX.empty();
        for (boolean value : values) {
            valueList.add(value);
        }
        return valueList;
    }

    static ListX<Integer> ofInts(int... values) {
        MutableListX<Integer> valueList = MutableListX.empty();
        for (int value : values) {
            valueList.add(value);
        }
        return valueList;
    }

    static ListX<Long> ofLongs(long... values) {
        MutableListX<Long> valueList = MutableListX.empty();
        for (long value : values) {
            valueList.add(value);
        }
        return valueList;
    }

    static ListX<Double> ofDoubles(double... values) {
        MutableListX<Double> valueList = MutableListX.empty();
        for (double value : values) {
            valueList.add(value);
        }
        return valueList;
    }

    static <E> ListX<E> build(Consumer<MutableListX<E>> listXConsumer) {
        MutableListX<E> list = MutableListX.empty();
        listXConsumer.accept(list);
        return list;
    }

    static <E> ListX<E> copyOf(Iterable<E> iterable) {
        return new ArrayListX<>(iterable);
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

    private MutableListX<E> takeLastToMutableList(int n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return MutableListX.empty();
        }
        MutableListX<E> list = MutableListX.of(this);
        int size = list.size();
        if (n >= size) {
            return list;
        }
        if (n == 1) {
            return MutableListX.of(last());
        }
        MutableListX<E> resultList = MutableListX.withInitCapacity(n);
        for (int index = size - n; index < size; index++) {
            resultList.add(list.get(index));
        }
        return resultList;
    }

    default ListX<E> takeLast(int n) {
        return takeLastToMutableList(n);
    }

    Optional<E> findRandom();

    default E random() {
        return findRandom().orElseThrow();
    }

    default Optional<E> findRandom(Random random) {
        return isNotEmpty() ? Optional.of(get(random.nextInt(size()))) : Optional.empty();
    }

    default E random(Random random) {
        return findRandom(random).orElseThrow();
    }

    default int binarySearchTo(int toIndex, ToIntFunction<E> comparison) {
        return binarySearch(0, toIndex, comparison);
    }

    default int binarySearchFrom(int fromIndex, ToIntFunction<E> comparison) {
        return binarySearch(fromIndex, size(), comparison);
    }

    default int binarySearch(ToIntFunction<E> comparison) {
        return binarySearch(0, size(), comparison);
    }

    /**
     * Searches this list or its range for an element for which the given [comparison] function
     * returns zero using the binary search algorithm.
     * <p>
     * The list is expected to be sorted so that the signs of the [comparison] function's return values ascend on the list elements,
     * i.e. negative values come before zero and zeroes come before positive values.
     * Otherwise, the result is undefined.
     * <p>
     * If the list contains multiple elements for which [comparison] returns zero, there is no guarantee which one will be found.
     *
     * @param comparison function that returns zero when called on the list element being searched.
     *                   On the elements coming before the target element, the function must return negative values;
     *                   on the elements coming after the target element, the function must return positive values.
     * @return the index of the found element, if it is contained in the list within the specified range;
     * otherwise, the inverted insertion point `(-insertion point - 1)`.
     * The insertion point is defined as the index at which the element should be inserted,
     * so that the list (or the specified subrange of list) still remains sorted.
     */
    int binarySearch(int fromIndex, int toIndex, ToIntFunction<E> comparison);

    @Override
    int size();

    int lastIndex();

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

    ListIterator<E> listIterator();

    ListIterator<E> listIterator(int index);

    ListX<E> headTo(int toIndex);

    ListX<E> tailFrom(int fromIndex);

    ListX<E> subList(int fromIndex, int toIndex);

    default ListX<E> skipLast(int n) {
        require(n >= 0, () -> "Requested element count " + n + " is less than zero.");
        return take(Math.max((size() - n), 0));
    }

    default ListX<E> skipLastWhile(@NotNull Predicate<E> predicate) {
        if (isEmpty()) {
            return MutableListX.empty();
        }
        ListIterator<E> iterator = listIterator(size());
        while (iterator.hasPrevious()) {
            if (!predicate.test(iterator.previous())) {
                return take(iterator.nextIndex() + 1L);
            }
        }
        return MutableListX.empty();
    }

    default ListX<E> takeLastWhile(@NotNull Predicate<E> predicate) {
        if (isEmpty()) {
            return MutableListX.empty();
        }
        ListIterator<E> iterator = listIterator(size());
        while (iterator.hasPrevious()) {
            if (!predicate.test(iterator.previous())) {
                iterator.next();
                int expectedSize = size() - iterator.nextIndex();
                if (expectedSize == 0) {
                    return MutableListX.empty();
                }
                MutableListX<E> result = MutableListX.withInitCapacity(expectedSize);
                while (iterator.hasNext()) {
                    result.add(iterator.next());
                }
                return result;
            }
        }
        return this;
    }

    @Override
    default @NotNull ListX<E> onEach(@NotNull Consumer<? super E> consumer) {
        return ListX.of(CollectionView.super.onEach(consumer));
    }

    @Override
    @NotNull
    default <R> ListX<E> onEach(@NotNull Function<? super E, ? extends R> selector, @NotNull Consumer<? super R> consumer) {
        return ListX.of(CollectionView.super.onEach(selector, consumer));
    }
}
