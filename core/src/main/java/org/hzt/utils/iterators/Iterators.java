package org.hzt.utils.iterators;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.function.IndexedBiFunction;
import org.hzt.utils.function.IndexedFunction;
import org.hzt.utils.iterators.functional_iterator.AtomicIterator;
import org.hzt.utils.spined_buffers.SpinedBuffer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Gatherer;

public final class Iterators {

    private Iterators() {
    }

    @SafeVarargs
    public static <T> Iterator<T> arrayIterator(final T... array) {
        return new ArrayIterator<>(array, false);
    }

    @SafeVarargs
    public static <T> Iterator<T> reverseArrayIterator(final T... array) {
        return new ArrayIterator<>(array, true);
    }

    public static <T> Iterator<T> reverseIterator(final List<T> list) {
        return reverseIterator(list.listIterator(list.size()));
    }

    public static <T> Iterator<T> reverseIterator(final ListX<T> list) {
        return reverseIterator(list.listIterator(list.size()));
    }

    private static <T> Iterator<T> reverseIterator(final ListIterator<T> listIterator) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return listIterator.hasPrevious();
            }

            @Override
            public T next() {
                return listIterator.previous();
            }
        };
    }

    public static <T> Iterator<T> generatorIterator(final Supplier<? extends T> initValueSupplier,
                                                    final UnaryOperator<T> nextValueSupplier) {
        return new GeneratorIterator<>(initValueSupplier, nextValueSupplier);
    }

    public static <T, R> Iterator<R> transformingIterator(final Iterator<T> iterator, final Function<? super T, ? extends R> mapper) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public R next() {
                return mapper.apply(iterator.next());
            }
        };
    }

    public static <T, R> Iterator<R> transformingIndexedIterator(final Iterator<T> iterator, final IndexedFunction<? super T, ? extends R> mapper) {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public R next() {
                final var prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                return mapper.apply(index++, iterator.next());
            }
        };
    }


    public static <T, R> Iterator<R> multiMappingIterator(final Iterator<T> iterator,
                                                          final BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return flatMappingIterator(iterator, e -> {
            final var spinedBuffer = new SpinedBuffer<R>();
            mapper.accept(e, spinedBuffer);
            return spinedBuffer.iterator();
        });
    }

    public static <T, A, R> Iterator<R> gatheringIterator(final Iterator<T> iterator,
                                                          final Gatherer<? super T, A, R> gatherer) {
        return new GatheringIterator<>(iterator, gatherer);
    }

    public static <T, R> Iterator<R> flatMappingIterator(final Iterator<T> iterator,
                                                         final Function<? super T, ? extends Iterator<? extends R>> toIteratorFunction) {
        return new FlatMappingIterator<>(iterator, toIteratorFunction);
    }

    public static <T> Iterator<T> filteringIterator(final Iterator<T> iterator, final Predicate<? super T> predicate, final boolean sendWhen) {
        return new FilteringIterator<>(iterator, predicate, sendWhen);
    }

    public static <T> Iterator<T> skipWhileIterator(final Iterator<T> iterator, final Predicate<? super T> predicate, final boolean inclusive) {
        return new SkipWhileIterator<>(iterator, predicate, inclusive);
    }

    public static <T> Iterator<T> takeWhileIterator(final Iterator<T> iterator, final Predicate<? super T> predicate, final boolean inclusive) {
        return new TakeWhileIterator<>(iterator, predicate, inclusive);
    }

    public static <T> Iterator<T> subIterator(final Iterator<T> iterator, final long startIndex, final long endIndex) {
        return new SubIterator<>(iterator, startIndex, endIndex);
    }

    public static <T> Iterator<ListX<T>> windowedIterator(final Iterator<T> iterator,
                                                          final int initSize,
                                                          final IntUnaryOperator nextSizeSupplier,
                                                          final int initStep,
                                                          final IntUnaryOperator nextStepSupplier,
                                                          final boolean partialWindows) {
        return new WindowedIterator<>(iterator, initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    public static <T, K> Iterator<T> distinctIterator(final Iterator<T> inputIterator, final Function<? super T, ? extends K> selector) {
        final var iterator = AtomicIterator.of(inputIterator);
        final Set<K> observed = new HashSet<>();
        final AtomicIterator<T> atomicIterator = action -> nextDistinctValue(iterator, observed, action, selector);
        return atomicIterator.asIterator();
    }

    private static <T, K> boolean nextDistinctValue(final AtomicIterator<T> iterator,
                                                    final Set<K> observed,
                                                    final Consumer<? super T> action,
                                                    final Function<? super T, ? extends K> selector) {
        final var reference = new AtomicReference<T>();
        while (iterator.tryAdvance(reference::set)) {
            final var next = reference.get();
            if (observed.add(selector.apply(next))) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    public static <T, A, R> Iterator<R> mergingIterator(final Iterator<T> thisIterator,
                                                        final Iterator<A> otherIterator,
                                                        final BiFunction<? super T, ? super A, ? extends R> transform) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return thisIterator.hasNext() && otherIterator.hasNext();
            }

            @Override
            public R next() {
                return transform.apply(thisIterator.next(), otherIterator.next());
            }
        };
    }

    public static <T> Iterator<T> interspersingIterator(final Iterator<T> iterator, final UnaryOperator<T> operator) {
        return new Iterator<>() {
            private T current = null;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                if (current != null) {
                    final var valueToInsert = operator.apply(current);
                    current = null;
                    return valueToInsert;
                } else {
                    current = iterator.next();
                    return current;
                }
            }
        };
    }

    public static <T> Iterator<T> interspersingIterator(final Iterator<T> iterator,
                                                        final Supplier<T> initValSupplier,
                                                        final UnaryOperator<T> operator) {
        return new Iterator<>() {
            private T valueToInsert = null;
            private boolean insertValue = false;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                if (insertValue) {
                    insertValue = false;
                    valueToInsert = valueToInsert == null ? initValSupplier.get() : operator.apply(this.valueToInsert);
                    return valueToInsert;
                } else {
                    insertValue = true;
                    return iterator.next();
                }
            }
        };
    }

    public static <T> Iterator<T> removingIterator(final Iterable<T> iterable, final T value) {
        final var removed = new AtomicBoolean();
        return filteringIterator(iterable.iterator(), e -> {
            if (!removed.get() && e == value) {
                removed.set(true);
                return false;
            } else {
                return true;
            }
        }, true);
    }

    public static <T, I extends Iterator<T>> I constrainOnceIterator(final I iterator, final AtomicBoolean consumed) {
        if (consumed.get()) {
            throw new IllegalStateException("Sequence is already consumed");
        }
        consumed.set(true);
        return iterator;
    }

    public static <T> PrimitiveIterator.OfInt indexIterator(final Iterator<T> iterator) {
        return new IndexIterator<>(iterator);
    }

    public static <R, T> Iterator<R> scanningIterator(final Iterator<T> iterator,
                                                      final R initial,
                                                      final IndexedBiFunction<? super R, ? super T, ? extends R> operation) {
        return new ScanningIterator<>(iterator, initial, operation);
    }
}
