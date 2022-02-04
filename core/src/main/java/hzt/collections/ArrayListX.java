package hzt.collections;

import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.ToIntFunction;

final class ArrayListX<T> implements MutableListX<T> {

    private static final Random RANDOM = new Random();

    private final List<T> list;

    ArrayListX() {
        this.list = new ArrayList<>();
    }

    ArrayListX(int initialCapacity) {
        this.list = new ArrayList<>(initialCapacity);
    }

    ArrayListX(Collection<T> collection) {
        this.list = new ArrayList<>(collection);
    }

    ArrayListX(Iterable<T> iterable) {
        list = new ArrayList<>();
        for (T t : iterable) {
            list.add(t);
        }
    }

    @SafeVarargs
    ArrayListX(T... values) {
        list = new ArrayList<>(values.length + 1);
        list.addAll(Arrays.asList(values));
    }

    ArrayListX(T value) {
        list = new ArrayList<>(1);
        list.add(value);
    }

    @Override
    public Optional<T> findRandom() {
        return isNotEmpty() ? Optional.of(get(RANDOM.nextInt(size()))) : Optional.empty();
    }

    @Override
    public int binarySearch(int fromIndex, int toIndex, ToIntFunction<T> comparison) {
        return ArrayHelper.binarySearch(size(), list::get, fromIndex, toIndex, comparison);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public int lastIndex() {
        return size() - 1;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object value) {
        return list.contains(value);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return list.toArray();
    }

    @NotNull
    @Override
    public <T1> T1 @NotNull [] toArray(@NotNull T1 @NotNull [] a) {
        //noinspection SuspiciousToArrayCall
        return list.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return list.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T set(int index, T element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
    }

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @NotNull
    @Override
    public MutableListX<T> headTo(int toIndex) {
        return subList(0, toIndex);
    }

    @NotNull
    @Override
    public MutableListX<T> tailFrom(int fromIndex) {
        return subList(fromIndex, size());
    }

    @NotNull
    @Override
    public MutableListX<T> subList(int fromIndex, int toIndex) {
        return MutableListX.of(list.subList(fromIndex, toIndex));
    }

    @Override
    public ListX<T> toListX() {
        return toListXOf(It::self);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArrayListX<?> that = (ArrayListX<?>) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
