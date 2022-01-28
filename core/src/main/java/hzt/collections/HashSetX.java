package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

final class HashSetX<E> implements MutableSetX<E> {

    private final Set<E> set;

    HashSetX() {
        this.set = new HashSet<>();
    }

    HashSetX(int n) {
        this.set = new HashSet<>(n);
    }

    HashSetX(Set<E> set) {
        this.set = set;
    }

    HashSetX(Collection<E> collection) {
        this.set = new HashSet<>(collection);
    }

    HashSetX(Iterable<E> iterable) {
        Set<E> newSet = new HashSet<>();
        iterable.forEach(newSet::add);
        this.set = newSet;
    }

    @SafeVarargs
    HashSetX(E @NotNull ... values) {
        Set<E> newSet = new HashSet<>();
        for (E item : values) {
            if (!newSet.add(item)) {
                throw new IllegalStateException("Duplicate elements in set. This is not allowed");
            }
        }
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HashSetX<?> hashSetX = (HashSetX<?>) o;
        return set.equals(hashSetX.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(set);
    }

    @Override
    public String toString() {
        return "HashSetX{" +
                "set=" + set +
                '}';
    }

    @Override
    public boolean isNotEmpty() {
        return !set.isEmpty();
    }

    @Override
    public boolean containsNot(E e) {
        return !contains(e);
    }
}
