package org.hzt.utils.iterators;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.function.IndexedFunction;
import org.hzt.utils.iterators.functional_iterator.AtomicIterator;
import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

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

public final class Iterators {

    private Iterators() {
    }

    @SafeVarargs
    public static <T>Iterator<T> arrayIterator(T... array) {
        return new ArrayIterator<>(array, false);
    }

    @SafeVarargs
    public static <T> Iterator<T> reverseArrayIterator(T... array) {
        return new ArrayIterator<>(array, true);
    }

    public static <T> Iterator<T> reverseIterator(List<T> list) {
        return reverseIterator(list.listIterator(list.size()));
    }

    public static <T> Iterator<T> reverseIterator(ListX<T> list) {
        return reverseIterator(list.listIterator(list.size()));
    }

    @NotNull
    private static <T> Iterator<T> reverseIterator(ListIterator<T> listIterator) {
        return new Iterator<T>() {
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

    public static <T> Iterator<T> generatorIterator(Supplier<? extends T> initValueSupplier,
                                                    UnaryOperator<T> nextValueSupplier) {
        return new GeneratorIterator<>(initValueSupplier, nextValueSupplier);
    }

    public static <T, R> @NotNull Iterator<R> transformingIterator(Iterator<T> iterator, Function<? super T, ? extends R> mapper) {
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

    public static <T, R> Iterator<R> transformingIndexedIterator(Iterator<T> iterator, IndexedFunction<? super T, ? extends R> mapper) {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public R next() {
                var prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                return mapper.apply(index++, iterator.next());
            }
        };
    }



    public static <T, R> Iterator<R> multiMappingIterator(@NotNull Iterator<T> iterator,
                                                          @NotNull BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return flatMappingIterator(iterator, e -> {
            final SpinedBuffer<R> spinedBuffer = new SpinedBuffer<>();
            mapper.accept(e, spinedBuffer);
            return spinedBuffer.iterator();
        });
    }

    public static <T, R> Iterator<R> flatMappingIterator(@NotNull Iterator<T> iterator,
                                                         @NotNull Function<? super T, ? extends Iterator<? extends R>> toIteratorFunction) {
        return new FlatMappingIterator<>(iterator, toIteratorFunction);
    }

    public static <T> Iterator<T> filteringIterator(Iterator<T> iterator, Predicate<? super T> predicate, boolean sendWhen) {
        return new FilteringIterator<>(iterator, predicate, sendWhen);
    }

    public static <T> Iterator<T> skipWhileIterator(Iterator<T> iterator, Predicate<? super T> predicate, boolean inclusive) {
        return new SkipWhileIterator<>(iterator, predicate, inclusive);
    }

    public static <T> Iterator<T> takeWhileIterator(Iterator<T> iterator, Predicate<? super T> predicate, boolean inclusive) {
        return new TakeWhileIterator<>(iterator, predicate, inclusive);
    }

    public static <T> Iterator<T> subIterator(Iterator<T> iterator, long startIndex, long endIndex) {
        return new SubIterator<>(iterator, startIndex, endIndex);
    }

    public static <T> Iterator<ListX<T>> windowedIterator(@NotNull Iterator<T> iterator,
                                                          int initSize,
                                                          @NotNull IntUnaryOperator nextSizeSupplier,
                                                          int initStep,
                                                          @NotNull IntUnaryOperator nextStepSupplier,
                                                          boolean partialWindows) {
        return new WindowedIterator<>(iterator, initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    @NotNull
    public static <T, K> Iterator<T> distinctIterator(Iterator<T> inputIterator, Function<? super T, ? extends K> selector) {
        final AtomicIterator<T> iterator = AtomicIterator.of(inputIterator);
        final Set<K> observed = new HashSet<>();
        final AtomicIterator<T> atomicIterator = action -> nextDistinctValue(iterator, observed, action, selector);
        return atomicIterator.asIterator();
    }

    private static <T, K> boolean nextDistinctValue(AtomicIterator<T> iterator,
                                                    Set<K> observed,
                                                    Consumer<? super T> action,
                                                    Function<? super T, ? extends K> selector) {
        AtomicReference<T> reference = new AtomicReference<>();
        while (iterator.tryAdvance(reference::set)) {
            T next = reference.get();
            if (observed.add(selector.apply(next))) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    public static <T, A, R> Iterator<R> mergingIterator(@NotNull Iterator<T> thisIterator,
                                                 @NotNull Iterator<A> otherIterator,
                                                 @NotNull BiFunction<? super T, ? super A, ? extends R> transform) {
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

    public static <T> Iterator<T> interspersingIterator(Iterator<T> iterator, UnaryOperator<T> operator) {
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

    public static <T> Iterator<T> interspersingIterator(Iterator<T> iterator,
                                                        Supplier<T> initValSupplier,
                                                        UnaryOperator<T> operator) {
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

    @NotNull
    public static <T> Iterator<T> removingIterator(Iterable<T> iterable, @NotNull T value) {
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

    @NotNull
    public static <T, I extends Iterator<T>> I constrainOnceIterator(I iterator, AtomicBoolean consumed) {
        if (consumed.get()) {
            throw new IllegalStateException("Sequence is already consumed");
        }
        consumed.set(true);
        return iterator;
    }

    public static <T> PrimitiveIterator.OfInt indexIterator(Iterator<T> iterator) {
        return new IndexIterator<>(iterator);
    }
}
