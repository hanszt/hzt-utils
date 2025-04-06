package org.hzt.utils.sequences;

import org.hzt.utils.collectors.Collector;
import org.hzt.utils.function.BiFunction;
import org.hzt.utils.function.Consumer;
import org.hzt.utils.function.Function;
import org.hzt.utils.function.Predicate;
import org.hzt.utils.iterators.AbstractIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A sequence is a simplified stream. It evaluates its operations in a lazy way.
 * <p>
 * It does not support parallel execution.
 * <p>
 * The implementation is heavily inspired on Kotlin's sequences api. This api provides offers simpler syntax than streams
 * and is easier to understand
 *
 * @param <T> the type of the items in the Sequence
 */
public abstract class Sequence<T> implements Iterable<T> {

    public abstract Iterator<T> iterator();

    public static <T> Sequence<T> empty() {
        return new Sequence<T>() {
            @Override
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() {

                    public boolean hasNext() {
                        return false;
                    }

                    public T next() {
                        throw new NoSuchElementException();
                    }
                };
            }
        };
    }

    public static <T> Builder<T> builder() {
        return new Builder<T>();
    }

    // Factory methods
    public static <T> Sequence<T> of(final Iterable<T> iterable) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return iterable.iterator();
            }
        };
    }

    public static <T> Sequence<T> of(final T... elements) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() {
                    int index = 0;

                    public boolean hasNext() {
                        return index < elements.length;
                    }

                    public T next() {
                        return elements[index++];
                    }
                };
            }
        };
    }

    // Infinite generator
    public static <T> Sequence<T> iterate(final T initial, final Function<T, T> nextItemGenerator) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() {
                    boolean hasNext = true;
                    T item = initial;

                    public boolean hasNext() {
                        if (hasNext) {
                            return true;
                        }
                        item = nextItemGenerator.apply(item);
                        hasNext = true;
                        return true;
                    }

                    public T next() {
                        if (hasNext()) {
                            hasNext = false;
                            return item;
                        }
                        throw new NoSuchElementException();
                    }
                };
            }
        };
    }

    // Intermediate ops
    public <R> Sequence<R> map(final Function<? super T, ? extends R> mapper) {
        return new Sequence<R>() {
            public Iterator<R> iterator() {
                final Iterator<T> iterator = Sequence.this.iterator();
                return new AbstractIterator<R>() {

                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public R next() {
                        return mapper.apply(iterator.next());
                    }
                };
            }
        };
    }

    public Sequence<T> filter(final Predicate<? super T> predicate) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                final Iterator<T> iterator = Sequence.this.iterator();
                return new AbstractIterator<T>() {
                    boolean hasNext = false;
                    T next = null;

                    public boolean hasNext() {
                        if (hasNext) {
                            return true;
                        }
                        while (iterator.hasNext()) {
                            next = iterator.next();
                            if (predicate.test(next)) {
                                hasNext = true;
                                return true;
                            }
                        }
                        return false;
                    }

                    public T next() {
                        if (hasNext()) {
                            hasNext = false;
                            return next;
                        }
                        throw new NoSuchElementException();
                    }
                };
            }
        };
    }

    public <R> Sequence<R> flatMap(final Function<? super T, ? extends Iterable<R>> mapper) {
        return new Sequence<R>() {
            public Iterator<R> iterator() {
                final Iterator<T> iterator = Sequence.this.iterator();
                return new AbstractIterator<R>() {
                    boolean hasNext = false;
                    R next = null;
                    Iterator<R> itemIterator = null;

                    public boolean hasNext() {
                        if (hasNext) {
                            return true;
                        }
                        if (itemIterator != null && itemIterator.hasNext()) {
                            hasNext = true;
                            next = itemIterator.next();
                            return true;
                        }
                        if (iterator.hasNext()) {
                            itemIterator = mapper.apply(iterator.next()).iterator();
                            if (itemIterator.hasNext()) {
                                hasNext = true;
                                next = itemIterator.next();
                            }
                        }
                        return hasNext;
                    }

                    public R next() {
                        if (hasNext()) {
                            hasNext = false;
                            return next;
                        }
                        throw new NoSuchElementException();
                    }
                };
            }
        };
    }

    public Sequence<T> take(final long count) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                final Iterator<T> iterator = Sequence.this.iterator();
                return new AbstractIterator<T>() {
                    boolean hasNext = false;
                    T next = null;
                    long counter = 0L;

                    public boolean hasNext() {
                        if (hasNext) {
                            return true;
                        }
                        if (iterator.hasNext() && counter < count) {
                            hasNext = true;
                            next = iterator.next();
                            return true;
                        }
                        return false;
                    }

                    public T next() {
                        if (hasNext()) {
                            hasNext = false;
                            counter++;
                            return next;
                        }
                        throw new NoSuchElementException();
                    }
                };
            }
        };
    }

    public Sequence<T> skip(final long count) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                final Iterator<T> iterator = Sequence.this.iterator();
                return new AbstractIterator<T>() {
                    boolean hasNext = false;
                    T next = null;
                    int counter = 0;

                    public boolean hasNext() {
                        if (hasNext) {
                            return true;
                        }
                        while (iterator.hasNext()) {
                            hasNext = true;
                            next = iterator.next();
                            if (counter == count) {
                                return true;
                            }
                            counter++;
                        }
                        return hasNext;
                    }

                    public T next() {
                        if (hasNext()) {
                            hasNext = false;
                            return next;
                        }
                        throw new NoSuchElementException();
                    }
                };
            }
        };
    }

    public Sequence<T> sorted(final Comparator<? super T> comparator) {
        return new Sequence<T>() {

            @Override
            public Iterator<T> iterator() {
                final List<T> list = new ArrayList<T>();
                for (final T t : Sequence.this) {
                    list.add(t);
                }
                Collections.sort(list, comparator);
                return list.iterator();
            }
        };
    }

    public Sequence<T> distinct() {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                final Iterator<T> iterator = Sequence.this.iterator();
                final Set<T> seen = new HashSet<T>();
                return new AbstractIterator<T>() {
                    boolean hasNext = false;
                    T next = null;

                    public boolean hasNext() {
                        if (hasNext) {
                            return true;
                        }
                        while (iterator.hasNext()) {
                            hasNext = true;
                            next = iterator.next();
                            if (!seen.contains(next)) {
                                return true;
                            }
                        }
                        return false;
                    }

                    public T next() {
                        if (hasNext()) {
                            final T next = iterator.next();
                            hasNext = false;
                            seen.add(next);
                            return next;
                        }
                        throw new NoSuchElementException();
                    }
                };
            }
        };
    }

    public <R> Sequence<R> andThen(SequenceExtension<T, R> extension) {
        return extension.extend(this);
    }

    // terminal ops
    public void forEach(final Consumer<? super T> action) {
        for (final T t : this) {
            action.accept(t);
        }
    }

    public T reduceOrNull(BiFunction<T, T, T> reducer) {
        final Iterator<T> iterator = iterator();
        if (iterator.hasNext()) {
            T acc = iterator.next();
            while (iterator.hasNext()) {
                acc = reducer.apply(acc, iterator.next());
            }
            return acc;
        }
        return null;
    }

    public <R> R fold(R initial, BiFunction<R, T, R> folder) {
        final Iterator<T> iterator = iterator();
        R acc = initial;
        while (iterator.hasNext()) {
            acc = folder.apply(acc, iterator.next());
        }
        return acc;
    }

    public <A, R> R collect(Collector<? super T, A, R> collector) {
        final Iterator<T> iterator = iterator();
        final A mutableContainer = collector.supply();
        while (iterator.hasNext()) {
            collector.accumulate(mutableContainer, iterator.next());
        }
        return collector.finish(mutableContainer);
    }


    public List<T> toList() {
        final List<T> result = new ArrayList<T>();
        for (final T t : this) {
            result.add(t);
        }
        return Collections.unmodifiableList(result);
    }

    // short-circuiting terminal ops
    public T firstOrNull() {
        final Iterator<T> iterator = iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public boolean any(final Predicate<? super T> predicate) {
        for (final T t : this) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

    public boolean any() {
        return iterator().hasNext();
    }

    public boolean all(final Predicate<? super T> predicate) {
        for (final T t : this) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    public boolean none(final Predicate<? super T> predicate) {
        for (final T t : this) {
            if (predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    public boolean none() {
        return !iterator().hasNext();
    }

    public static class Builder<T> {
        private final List<T> buffer = new ArrayList<T>();

        private Builder() {
        }

        public void add(T item) {
            buffer.add(item);
        }

        public Sequence<T> build() {
            return new Sequence<T>() {

                public Iterator<T> iterator() {
                    return buffer.iterator();
                }
            };
        }
    }
}
