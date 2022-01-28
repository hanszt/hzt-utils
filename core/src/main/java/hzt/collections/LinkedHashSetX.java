package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

final class LinkedHashSetX<E> implements MutableLinkedSetX<E> {

    private final Set<E> set;

    LinkedHashSetX() {
        this.set = new LinkedHashSet<>();
    }

    LinkedHashSetX(int n) {
        this.set = new LinkedHashSet<>(n);
    }

    LinkedHashSetX(Set<E> set) {
        this.set = set;
    }

    LinkedHashSetX(Collection<E> collection) {
        this.set = new LinkedHashSet<>(collection);
    }

    LinkedHashSetX(Iterable<E> iterable) {
        Set<E> newSet = new LinkedHashSet<>();
        iterable.forEach(newSet::add);
        this.set = newSet;
    }

    @SafeVarargs
    LinkedHashSetX(E first, E @NotNull ... others) {
        Set<E> newSet = new LinkedHashSet<>();
        newSet.add(first);
        Collections.addAll(newSet, others);
        this.set = newSet;
    }

    @Override
    public Iterable<E> iterable() {
        return set;
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return set.toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        //noinspection SuspiciousToArrayCall
        return set.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return set.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        return set.addAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public void clear() {
        set.clear();
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
