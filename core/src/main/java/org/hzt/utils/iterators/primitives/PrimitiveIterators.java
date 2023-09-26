package org.hzt.utils.iterators.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.DoubleMutableSet;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableCollection;
import org.hzt.utils.collections.primitives.IntMutableSet;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.collections.primitives.LongMutableSet;
import org.hzt.utils.function.primitives.DoubleIndexedFunction;
import org.hzt.utils.function.primitives.LongIndexedFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import static java.util.PrimitiveIterator.OfDouble;
import static java.util.PrimitiveIterator.OfInt;
import static java.util.PrimitiveIterator.OfLong;

@SuppressWarnings("squid:S1200")
public final class PrimitiveIterators {

    private static final String ITERATOR_INDEX_OVERFLOW = "Iterator index overflow";

    private PrimitiveIterators() {
    }

    @NotNull
    public static OfInt intArrayIterator(final int @NotNull ... array) {
        return new OfInt() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public int nextInt() {
                final var prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException(getMessage(index));
                }
                return array[index++];
            }
        };
    }

    @NotNull
    public static <T> OfInt intIteratorOf(@NotNull final Iterator<T> iterator,
                                                            @NotNull final ToIntFunction<? super T> mapper) {
        return new OfInt() {
            @Override
            public int nextInt() {
                if (iterator instanceof final OfInt intIterator) {
                    return intIterator.nextInt();
                }
                return mapper.applyAsInt(iterator.next());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    public static <T> OfInt toIntFlatMappingIterator(@NotNull final Iterator<T> iterator,
                                                                       @NotNull final Function<? super T,
                                                                               ? extends OfInt> mapper) {
        return new ToIntFlatMappingIterator<>(iterator, mapper);
    }

    public static <T> OfLong toLongFlatMappingIterator(@NotNull final Iterator<T> iterator,
                                                                         @NotNull final Function<? super T,
                                                                                 ? extends OfLong> mapper) {
        return new ToLongFlatMappingIterator<>(iterator, mapper);
    }

    public static <T> OfDouble toDoubleFlatMappingIterator(@NotNull final Iterator<T> iterator,
                                                                             @NotNull final Function<? super T,
                                                                                     ? extends OfDouble> mapper) {
        return new ToDoubleFlatMappingIterator<>(iterator, mapper);
    }

    @NotNull
    public static OfInt intTransformingIterator(@NotNull final OfInt iterator,
                                                                  @NotNull final IntUnaryOperator mapper) {
        return new OfInt() {
            @Override
            public int nextInt() {
                return mapper.applyAsInt(iterator.nextInt());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    @NotNull
    public static OfInt intIndexedTransformingIterator(final OfInt iterator, final IntBinaryOperator mapper) {
        return new OfInt() {

            int index = 0;

            @Override
            public int nextInt() {
                if (index < 0) {
                    throw new NoSuchElementException(ITERATOR_INDEX_OVERFLOW);
                }
                return mapper.applyAsInt(index++, iterator.nextInt());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    @NotNull
    public static OfLong intToLongIterator(@NotNull final OfInt iterator,
                                                             @NotNull final IntToLongFunction mapper) {
        return new OfLong() {
            @Override
            public long nextLong() {
                return mapper.applyAsLong(iterator.nextInt());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    @NotNull
    public static OfDouble intToDoubleIterator(@NotNull final OfInt iterator,
                                                                 @NotNull final IntToDoubleFunction mapper) {
        return new OfDouble() {
            @Override
            public double nextDouble() {
                return mapper.applyAsDouble(iterator.nextInt());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    public static <R> Iterator<R> intToObjIterator(@NotNull final OfInt iterator,
                                                   @NotNull final IntFunction<? extends R> mapper) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            @SuppressWarnings("squid:S2272")
            public R next() {
                return mapper.apply(iterator.nextInt());
            }
        };
    }

    @NotNull
    public static OfLong longArrayIterator(final long @NotNull ... array) {
        return new OfLong() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public long nextLong() {
                final var prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException(getMessage(index));
                }
                return array[index++];
            }
        };
    }

    @NotNull
    public static <T> OfLong longIteratorOf(final Iterator<T> iterator, final ToLongFunction<? super T> mapper) {
        return new OfLong() {
            @Override
            public long nextLong() {
                if (iterator instanceof final OfLong longIterator) {
                    return longIterator.nextLong();
                }
                return mapper.applyAsLong(iterator.next());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    @NotNull
    public static OfLong longTransformingIterator(final OfLong iterator, final LongUnaryOperator mapper) {
        return new OfLong() {
            @Override
            public long nextLong() {
                return mapper.applyAsLong(iterator.nextLong());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    @NotNull
    public static OfLong longIndexedTransformingIterator(final OfLong iterator, final LongIndexedFunction mapper) {
        return new OfLong() {

            int index = 0;

            @Override
            public long nextLong() {
                if (index < 0) {
                    throw new NoSuchElementException(ITERATOR_INDEX_OVERFLOW);
                }
                return mapper.applyAsLong(index++, iterator.nextLong());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    @NotNull
    public static OfInt longToIntIterator(@NotNull final OfLong iterator,
                                                            @NotNull final LongToIntFunction mapper) {
        return new OfInt() {
            @Override
            public int nextInt() {
                return mapper.applyAsInt(iterator.nextLong());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    @NotNull
    public static OfDouble longToDoubleIterator(@NotNull final OfLong iterator,
                                                                  @NotNull final LongToDoubleFunction mapper) {
        return new OfDouble() {
            @Override
            public double nextDouble() {
                return mapper.applyAsDouble(iterator.nextLong());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    public static <R> Iterator<R> longToObjIterator(@NotNull final OfLong iterator,
                                                    @NotNull final LongFunction<? extends R> mapper) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            @SuppressWarnings("squid:S2272")
            public R next() {
                return mapper.apply(iterator.nextLong());
            }
        };
    }

    @NotNull
    public static OfDouble doubleArrayIterator(final double @NotNull ... array) {
        return new OfDouble() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public double nextDouble() {
                final var prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException(getMessage(index));
                }
                return array[index++];
            }
        };
    }

    @NotNull
    public static <T> OfDouble doubleIteratorOf(@NotNull final Iterator<T> iterator,
                                                                  @NotNull final ToDoubleFunction<? super T> mapper) {
        return new OfDouble() {
            @Override
            public double nextDouble() {
                return switch (iterator) {
                    case final OfDouble doubleIterator -> doubleIterator.nextDouble();
                    case final OfInt intIterator -> intToDoubleIterator(intIterator, It::asDouble).nextDouble();
                    case final OfLong longIterator -> longToDoubleIterator(longIterator, It::asDouble).nextDouble();
                    default -> mapper.applyAsDouble(iterator.next());
                };
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    @NotNull
    public static OfDouble doubleTransformingIterator(@NotNull final OfDouble iterator,
                                                                        @NotNull final DoubleUnaryOperator mapper) {
        return new OfDouble() {
            @Override
            public double nextDouble() {
                return mapper.applyAsDouble(iterator.nextDouble());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    @NotNull
    public static OfDouble doubleIndexedTransformingIterator(@NotNull final OfDouble iterator,
                                                                               @NotNull final DoubleIndexedFunction mapper) {
        return new OfDouble() {

            int index = 0;

            @Override
            public double nextDouble() {
                if (index < 0) {
                    throw new NoSuchElementException(ITERATOR_INDEX_OVERFLOW);
                }
                return mapper.applyAsDouble(index++, iterator.nextDouble());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    public static OfInt doubleToIntIterator(@NotNull final OfDouble doubleIterator,
                                                              @NotNull final DoubleToIntFunction mapper) {
        return new OfInt() {
            @Override
            public int nextInt() {
                return mapper.applyAsInt(doubleIterator.nextDouble());
            }

            @Override
            public boolean hasNext() {
                return doubleIterator.hasNext();
            }
        };
    }

    public static OfLong doubleToLongIterator(@NotNull final OfDouble doubleIterator,
                                                                @NotNull final DoubleToLongFunction mapper) {
        return new OfLong() {
            @Override
            public long nextLong() {
                return mapper.applyAsLong(doubleIterator.nextDouble());
            }

            @Override
            public boolean hasNext() {
                return doubleIterator.hasNext();
            }
        };
    }

    public static <R> Iterator<R> doubleToObjIterator(@NotNull final OfDouble iterator,
                                                      @NotNull final DoubleFunction<? extends R> mapper) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            @SuppressWarnings("squid:S2272")
            public R next() {
                return mapper.apply(iterator.nextDouble());
            }
        };
    }

    public static OfInt mergingIterator(
            @NotNull final OfInt iterator,
            @NotNull final OfInt otherIterator,
            @NotNull final IntBinaryOperator merger) {
        return new OfInt() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext() && otherIterator.hasNext();
            }

            @Override
            public int nextInt() {
                return merger.applyAsInt(iterator.nextInt(), otherIterator.nextInt());
            }
        };
    }

    public static OfLong mergingIterator(
            @NotNull final OfLong iterator,
            @NotNull final OfLong otherIterator,
            @NotNull final LongBinaryOperator merger) {
        return new OfLong() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext() && otherIterator.hasNext();
            }

            @Override
            public long nextLong() {
                return merger.applyAsLong(iterator.nextLong(), otherIterator.nextLong());
            }
        };
    }

    public static OfDouble mergingIterator(
            @NotNull final OfDouble iterator,
            @NotNull final OfDouble otherIterator,
            @NotNull final DoubleBinaryOperator merger) {
        return new OfDouble() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext() && otherIterator.hasNext();
            }

            @Override
            public double nextDouble() {
                return merger.applyAsDouble(iterator.nextDouble(), otherIterator.nextDouble());
            }
        };
    }

    public static @NotNull OfInt emptyIntIterator() {
        return new OfInt() {
            @Override
            public int nextInt() {
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        };
    }

    public static @NotNull OfLong emptyLongIterator() {
        return new OfLong() {
            @Override
            public long nextLong() {
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        };
    }

    public static @NotNull OfDouble emptyDoubleIterator() {
        return new OfDouble() {
            @Override
            public double nextDouble() {
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        };
    }

    @NotNull
    public static OfInt distinctIterator(@NotNull final OfInt iterator) {
        final var observed = IntMutableSet.empty();
        final PrimitiveAtomicIterator.OfInt iteratorX = action -> nextDistinctInt(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctInt(@NotNull final OfInt iterator,
                                           @NotNull final IntMutableCollection observed,
                                           @NotNull final IntConsumer action) {
        while (iterator.hasNext()) {
            final var next = iterator.nextInt();
            if (observed.add(next)) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    @NotNull
    public static OfLong distinctIterator(@NotNull final OfLong iterator) {
        final var observed = LongMutableSet.empty();
        final PrimitiveAtomicIterator.OfLong iteratorX = action -> nextDistinctLong(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctLong(@NotNull final OfLong iterator,
                                            @NotNull final LongMutableSet observed,
                                            @NotNull final LongConsumer action) {
        while (iterator.hasNext()) {
            final var next = iterator.nextLong();
            if (observed.add(next)) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    @NotNull
    public static OfDouble distinctIterator(@NotNull final OfDouble iterator) {
        final var observed = DoubleMutableSet.empty();
        final PrimitiveAtomicIterator.OfDouble iteratorX = action -> nextDistinctDouble(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctDouble(@NotNull final OfDouble iterator,
                                              @NotNull final DoubleMutableSet observed,
                                              @NotNull final DoubleConsumer action) {
        while (iterator.hasNext()) {
            final var next = iterator.nextDouble();
            if (observed.add(next)) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    public static OfInt intScanningIterator(final OfInt iterator,
                                                              final int initial, final IntBinaryOperator operation) {
        return new IntScanningIterator(iterator, initial, operation);
    }

    public static OfLong longScanningIterator(final OfLong iterator,
                                                                final long initial, final LongBinaryOperator operation) {
        return new LongScanningIterator(iterator, initial, operation);
    }

    public static OfDouble doubleScanningIterator(final OfDouble iterator,
                                                                    final double initial, final DoubleBinaryOperator operation) {
        return new DoubleScanningIterator(iterator, initial, operation);
    }

    @NotNull
    private static String getMessage(final int index) {
        return "index out of bounds. (Index value: " + index + ")";
    }

    public static OfInt reverseIterator(final IntList doubleList) {
        final var listIterator = doubleList.listIterator(doubleList.size());
        return new OfInt() {
            @Override
            public boolean hasNext() {
                return listIterator.hasPrevious();
            }

            @Override
            public int nextInt() {
                return listIterator.previousInt();
            }
        };
    }

    public static OfLong reverseIterator(final LongList doubleList) {
        final var listIterator = doubleList.listIterator(doubleList.size());
        return new OfLong() {

            @Override
            public boolean hasNext() {
                return listIterator.hasPrevious();
            }

            @Override
            public long nextLong() {
                return listIterator.previousLong();
            }
        };
    }

    public static OfDouble reverseIterator(final DoubleList doubleList) {
        final var listIterator = doubleList.listIterator(doubleList.size());
        return new OfDouble() {

            @Override
            public boolean hasNext() {
                return listIterator.hasPrevious();
            }

            @Override
            public double nextDouble() {
                return listIterator.previousDouble();
            }
        };
    }
}
