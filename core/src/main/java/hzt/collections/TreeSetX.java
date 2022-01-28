package hzt.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import static java.util.Comparator.comparing;

final class TreeSetX<E, R extends Comparable<R>> implements NavigableSetX<E> {

    private final NavigableSet<E> navigableSet;

    TreeSetX(Function<? super E, ? extends R> selector) {
        this.navigableSet = new TreeSet<>(comparing(selector));
    }

    TreeSetX(NavigableSet<E> navigableSet) {
        this.navigableSet = navigableSet;
    }

    TreeSetX(Collection<E> collection, Function<E, R> selector) {
        this.navigableSet = new TreeSet<>(comparing(selector));
        navigableSet.addAll(collection);
    }

    TreeSetX(Iterable<E> iterable, Function<? super E, ? extends R> selector) {
        TreeSet<E> newSet = new TreeSet<E>(comparing(selector));
        iterable.forEach(newSet::add);
        this.navigableSet = newSet;
    }

    @SafeVarargs
    TreeSetX(Function<E, R> selector, E first, E @NotNull ... others) {
        TreeSet<E> newSet = new TreeSet<>(comparing(selector));
        newSet.add(first);
        Collections.addAll(newSet, others);
        this.navigableSet = newSet;
    }

    @Override
    public Iterable<E> iterable() {
        return navigableSet;
    }

    @Override
    public int size() {
        return navigableSet.size();
    }

    @Override
    public boolean isEmpty() {
        return navigableSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return navigableSet.contains(o);
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return navigableSet.toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        //noinspection SuspiciousToArrayCall
        return navigableSet.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return navigableSet.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return navigableSet.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return navigableSet.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        return navigableSet.addAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return navigableSet.retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return navigableSet.removeAll(c);
    }

    @Override
    public void clear() {
        navigableSet.clear();
    }

    @Nullable
    @Override
    public Comparator<? super E> comparator() {
        return navigableSet.comparator();
    }

    @Nullable
    @Override
    public E lower(E e) {
        return navigableSet.lower(e);
    }

    @Nullable
    @Override
    public E floor(E e) {
        return navigableSet.floor(e);
    }

    @Nullable
    @Override
    public E ceiling(E e) {
        return navigableSet.ceiling(e);
    }

    @Nullable
    @Override
    public E higher(E e) {
        return navigableSet.higher(e);
    }

    @Nullable
    @Override
    public E pollFirst() {
        return navigableSet.pollFirst();
    }

    @Nullable
    @Override
    public E pollLast() {
        return navigableSet.pollLast();
    }

    @NotNull
    @Override
    public NavigableSet<E> descendingSet() {
        return navigableSet.descendingSet();
    }

    @NotNull
    @Override
    public Iterator<E> descendingIterator() {
        return navigableSet.descendingIterator();
    }

    @NotNull
    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return navigableSet.subSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    @NotNull
    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return navigableSet.headSet(toElement, inclusive);
    }

    @NotNull
    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return navigableSet.tailSet(fromElement, inclusive);
    }

    @NotNull
    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return navigableSet.subSet(fromElement, toElement);
    }

    @NotNull
    @Override
    public SortedSet<E> headSet(E toElement) {
        return navigableSet.headSet(toElement);
    }

    @NotNull
    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return navigableSet.tailSet(fromElement);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TreeSetX<?, ?> treeSetX = (TreeSetX<?, ?>) o;
        return navigableSet.equals(treeSetX.navigableSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(navigableSet);
    }

    @Override
    public String toString() {
        return "TreeSetX{" +
                "items=" + navigableSet +
                '}';
    }

    @Override
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    @Override
    public boolean containsNot(E e) {
        return !contains(e);
    }
}
