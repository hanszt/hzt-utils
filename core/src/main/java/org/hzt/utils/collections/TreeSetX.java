package org.hzt.utils.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import static java.util.Comparator.comparing;

final class TreeSetX<E, R extends Comparable<? super R>> extends AbstractSet<E> implements SortedMutableSetX<E> {

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
    TreeSetX(final Function<E, R> selector, final E first, final E... others) {
        navigableSet = new TreeSet<>(comparing(selector));
        navigableSet.add(first);
        Collections.addAll(navigableSet, others);
    }

    @Override
    public int size() {
        return navigableSet.size();
    }

    @Override
    public boolean contains(final Object value) {
        return navigableSet.contains(value);
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
    public boolean containsAll(final Collection<?> c) {
        return navigableSet.containsAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return navigableSet.retainAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return navigableSet.removeAll(c);
    }

    @Override
    public Comparator<? super E> comparator() {
        return navigableSet.comparator();
    }

    @Override
    public E lower(final E e) {
        return navigableSet.lower(e);
    }

    @Override
    public E floor(final E e) {
        return navigableSet.floor(e);
    }

    @Override
    public E ceiling(final E e) {
        return navigableSet.ceiling(e);
    }

    @Override
    public E higher(final E e) {
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
    public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        return navigableSet.subSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    @Override
    public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
        return navigableSet.headSet(toElement, inclusive);
    }

    @Override
    public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
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
    public SortedSet<E> subSet(final E fromElement, final E toElement) {
        return navigableSet.subSet(fromElement, toElement);
    }

    @Override
    public SortedSet<E> headSet(final E toElement) {
        return navigableSet.headSet(toElement);
    }

    @Override
    public SortedSet<E> tailSet(final E fromElement) {
        return navigableSet.tailSet(fromElement);
    }
}
