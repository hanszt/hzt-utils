package org.hzt.utils.collections;

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

    TreeSetX(Function<? super E, ? extends R> selector) {
        this.navigableSet = new TreeSet<>(comparing(selector));
    }

    TreeSetX(NavigableSet<E> navigableSet) {
        this.navigableSet = new TreeSet<>(navigableSet);
    }

    TreeSetX(Collection<E> collection, Function<E, R> selector) {
        this.navigableSet = new TreeSet<>(comparing(selector));
        navigableSet.addAll(collection);
    }

    TreeSetX(Iterable<E> iterable, Function<? super E, ? extends R> selector) {
        navigableSet = new TreeSet<>(comparing(selector));
        iterable.forEach(navigableSet::add);
    }

    @SafeVarargs
    TreeSetX(Function<E, R> selector, E first, E... others) {
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
    public boolean contains(Object value) {
        return navigableSet.contains(value);
    }

    @Override
    public Object[] toArray() {
        return navigableSet.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
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
    public boolean containsAll(Collection<?> c) {
        return navigableSet.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return navigableSet.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return navigableSet.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return navigableSet.removeAll(c);
    }

    @Override
    public void clear() {
        navigableSet.clear();
    }

    @Override
    public Comparator<? super E> comparator() {
        return navigableSet.comparator();
    }

    @Override
    public E lower(E e) {
        return navigableSet.lower(e);
    }

    @Override
    public E floor(E e) {
        return navigableSet.floor(e);
    }

    @Override
    public E ceiling(E e) {
        return navigableSet.ceiling(e);
    }

    @Override
    public E higher(E e) {
        return navigableSet.higher(e);
    }

    @Override
    public E pollFirst() {
        return navigableSet.pollFirst();
    }

    @Override
    public E pollLast() {
        return navigableSet.pollLast();
    }

    @Override
    public Iterator<E> iterator() {
        return navigableSet.iterator();
    }

    @Override
    public NavigableSet<E> descendingSet() {
        return navigableSet.descendingSet();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return navigableSet.descendingIterator();
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return navigableSet.subSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return navigableSet.headSet(toElement, inclusive);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return navigableSet.tailSet(fromElement, inclusive);
    }

    @Override
    public E first() {
        return navigableSet.first();
    }

    @Override
    public E last() {
        return navigableSet.last();
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return navigableSet.subSet(fromElement, toElement);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return navigableSet.headSet(toElement);
    }

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
        return navigableSet.toString();
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
