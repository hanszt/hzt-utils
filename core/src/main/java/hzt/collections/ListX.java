package hzt.collections;

import hzt.function.It;
import hzt.strings.StringX;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 * This class represents an immutable non-null list. When a list of this interface is created, all null values are filtered out
 *
 * @param <E> the type of the elements
 * @author Hans Zuidervaart
 */
public interface ListX<E> extends CollectionView<E> {

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
        MutableListX<Boolean> valueList = MutableListX.<Boolean>empty();
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
        MutableListX<Long> valueList = MutableListX.<Long>empty();
        for (long value : values) {
            valueList.add(value);
        }
        return valueList;
    }

    static ListX<Double> ofDoubles(double... values) {
        MutableListX<Double> valueList = MutableListX.<Double>empty();
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

    @Override
    default <R> ListX<R> castIfInstanceOf(Class<R> aClass) {
        return castToMutableListIfInstanceOf(aClass);
    }

    @Override
    default <R> ListX<R> mapFiltering(Function<? super E, ? extends R> mapper, Predicate<R> resultFilter) {
        return mapFiltering(It.noFilter(), mapper, resultFilter);
    }

    @Override
    default <R> ListX<R> filterMapping(Predicate<E> predicate, Function<E, R> mapper) {
        return mapFiltering(predicate, mapper, It.noFilter());
    }

    @Override
    default <R> ListX<R> mapFiltering(Predicate<E> predicate, Function<? super E, ? extends R> mapper, Predicate<R> resultFilter) {
        return mapFilteringToCollection(MutableListX::of, predicate, mapper, resultFilter);
    }

    default MutableListX<E> toMutableList() {
        return MutableListX.of(this);
    }

    default List<E> toList() {
        return toListOf(It::self);
    }

    default SetX<E> toSetMutableSet() {
        return toMutableSetOf(It::self);
    }

    default SetX<E> toSetX() {
        return toMutableSetNotNullOf(It::self);
    }

    default Set<E> toSet() {
        return toMutableSetNotNullOf(It::self);
    }

    @Override
    default ListX<E> plus(@NotNull Iterable<E> values) {
        return toMutableListPlus(values);
    }

    @Override
    default ListX<E> plus(E value) {
        return toMutableListPlus(value);
    }

    @Override
    default <R> ListX<R> map(Function<E, R> mapper) {
        return toMutableListOf(mapper);
    }

    default ListX<StringX> toStringXList(Function<E, CharSequence> mapper) {
        return map(s -> StringX.of(mapper.apply(s)));
    }

    @Override
    default ListX<E> filter(Predicate<E> predicate) {
        return filterToMutableList(predicate);
    }

    @Override
    default ListX<E> filterNot(Predicate<E> predicate) {
        return filterToListX(predicate.negate());
    }

    @Override
    default ListX<E> takeWhile(Predicate<E> predicate) {
        return takeToListXWhile(predicate);
    }

    static <E> ListX<E> copyOf(Iterable<E> iterable) {
        return new ArrayListX<>(iterable);
    }

    default <R> R foldRight(@NotNull R initial, @NotNull BiFunction<E, R, R> operation) {
        ListX<E> list = getListOrElseCompute();
        R accumulator = initial;
        if (list.isNotEmpty()) {
            final ListIterator<E> listIterator = list.listIterator(list.size());
            while (listIterator.hasPrevious()) {
                accumulator = operation.apply(listIterator.previous(), accumulator);
            }
        }
        return accumulator;
    }

    default MutableListX<E> takeLastToMutableList(int n) {
        IterableX.requireGreaterThanZero(n);
        if (n == 0) {
            return MutableListX.empty();
        }
        MutableListX<E> list = getListOrElseCompute();
        int size = list.size();
        if (n >= size) {
            return list;
        }
        if (n == 1) {
            return MutableListX.of(last());
        }
        MutableListX<E> resultList = MutableListX.<E>withInitCapacity(n);
        for (int index = size - n; index < size; index++) {
            resultList.add(list.get(index));
        }
        return resultList;
    }

    default ListX<E> takeLast(int n) {
        return takeLastToMutableList(n);
    }

    @Override
    default <R> ListX<E> distinctBy(Function<E, R> selector) {
        return distinctToMutableListBy(selector);
    }

    E random();

    default E random(Random random) {
        return get(random.nextInt(size()));
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

    int size();

    int lastIndex();

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    boolean contains(Object o);

    @Override
    default boolean containsNot(E e) {
        return !contains(e);
    }

    boolean containsAll(Collection<?> c);

    default boolean containsNoneOf(Collection<?> collection) {
        return !containsAll(collection);
    }

    E get(int index);

    int indexOf(Object o);

    int lastIndexOf(Object o);

    ListIterator<E> listIterator();

    ListIterator<E> listIterator(int index);

    ListX<E> subList(int fromIndex, int toIndex);
}
