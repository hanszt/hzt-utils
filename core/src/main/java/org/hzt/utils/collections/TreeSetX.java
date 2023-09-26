package org.hzt.utils.collections;

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

final class TreeSetX<E, R extends Comparable<? super R>> implements SortedMutableSetX<E> {

    private final NavigableSet<E> navigableSet;

    TreeSetX(final Function<? super E, ? extends R> selector) {
        this.navigableSet = new TreeSet<>(comparing(selector));
    }

    TreeSetX(final NavigableSet<E> navigableSet) {
        this.navigableSet = new TreeSet<>(navigableSet);
    }

    TreeSetX(final Collection<E> collection, final Function<E, R> selector) {
        this.navigableSet = new TreeSet<>(comparing(selector));
        navigableSet.addAll(collection);
    }

    TreeSetX(final Iterable<E> iterable, final Function<? super E, ? extends R> selector) {
        navigableSet = new TreeSet<>(comparing(selector));
        iterable.forEach(navigableSet::add);
    }

    @SafeVarargs
    TreeSetX(final Function<E, R> selector, final E first, final E @NotNull ... others) {
        navigableSet = new TreeSet<>(comparing(selector));
        navigableSet.add(first);
        Collections.addAll(navigableSet, others);
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
    public boolean contains(final Object value) {
        return navigableSet.contains(value);
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return navigableSet.toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull final T @NotNull [] a) {
        //noinspection SuspiciousToArrayCall
        return navigableSet.toArray(a);
    }

    @Override
    public boolean add(final E e) {
        return navigableSet.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return navigableSet.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull final Collection<?> c) {
        return navigableSet.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull final Collection<? extends E> c) {
        return navigableSet.addAll(c);
    }

    @Override
    public boolean retainAll(@NotNull final Collection<?> c) {
        return navigableSet.retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull final Collection<?> c) {
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
    public E lower(final E e) {
        return navigableSet.lower(e);
    }

    @Nullable
    @Override
    public E floor(final E e) {
        return navigableSet.floor(e);
    }

    @Nullable
    @Override
    public E ceiling(final E e) {
        return navigableSet.ceiling(e);
    }

    @Nullable
    @Override
    public E higher(final E e) {
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
    public Iterator<E> iterator() {
        return navigableSet.iterator();
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
    public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        return navigableSet.subSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    @NotNull
    @Override
    public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
        return navigableSet.headSet(toElement, inclusive);
    }

    @NotNull
    @Override
    public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
        return navigableSet.tailSet(fromElement, inclusive);
    }

    @Override
    public @NotNull E first() {
        return navigableSet.first();
    }

    @Override
    public @NotNull E last() {
        return navigableSet.last();
    }

    @NotNull
    @Override
    public SortedSet<E> subSet(final E fromElement, final E toElement) {
        return navigableSet.subSet(fromElement, toElement);
    }

    @NotNull
    @Override
    public SortedSet<E> headSet(final E toElement) {
        return navigableSet.headSet(toElement);
    }

    @NotNull
    @Override
    public SortedSet<E> tailSet(final E fromElement) {
        return navigableSet.tailSet(fromElement);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var treeSetX = (TreeSetX<?, ?>) o;
        return navigableSet.equals(treeSetX.navigableSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(navigableSet);
    }

    @Override
    public String toString() {
        return navigableSet.toString();
    }

    @Override
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    @Override
    public boolean containsNot(final E e) {
        return !contains(e);
    }
}
