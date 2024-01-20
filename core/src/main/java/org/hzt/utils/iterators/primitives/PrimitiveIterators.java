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
import org.hzt.utils.gatherers.primitives.IntGatherer;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Queue;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongSupplier;
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

    public static PrimitiveIterator.OfInt generatorIterator(final IntSupplier initSupplier, final IntUnaryOperator nextSupplier) {
        return new IntGeneratorIterator(initSupplier, nextSupplier);
    }

    public static PrimitiveIterator.OfLong generatorIterator(final LongSupplier initSupplier, final LongUnaryOperator nextSupplier) {
        return new LongGeneratorIterator(initSupplier, nextSupplier);
    }

    public static PrimitiveIterator.OfDouble generatorIterator(final DoubleSupplier initSupplier, final DoubleUnaryOperator nextSupplier) {
        return new DoubleGeneratorIterator(initSupplier, nextSupplier);
    }

    public static OfInt intArrayIterator(final int... array) {
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

    public static <T> OfInt intIteratorOf(final Iterator<T> iterator,
                                                            final ToIntFunction<? super T> mapper) {
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

    public static <T> OfInt toIntFlatMappingIterator(final Iterator<T> iterator,
                                                                       final Function<? super T,
                                                                               ? extends OfInt> mapper) {
        return new ToIntFlatMappingIterator<>(iterator, mapper);
    }

    public static <T> OfLong toLongFlatMappingIterator(final Iterator<T> iterator,
                                                                         final Function<? super T,
                                                                                 ? extends OfLong> mapper) {
        return new ToLongFlatMappingIterator<>(iterator, mapper);
    }

    public static <T> OfDouble toDoubleFlatMappingIterator(final Iterator<T> iterator,
                                                                             final Function<? super T,
                                                                                     ? extends OfDouble> mapper) {
        return new ToDoubleFlatMappingIterator<>(iterator, mapper);
    }

    public static OfInt intTransformingIterator(final OfInt iterator,
                                                                  final IntUnaryOperator mapper) {
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

    public static OfLong intToLongIterator(final OfInt iterator,
                                                             final IntToLongFunction mapper) {
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

    public static OfDouble intToDoubleIterator(final OfInt iterator,
                                                                 final IntToDoubleFunction mapper) {
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

    public static <R> Iterator<R> intToObjIterator(final OfInt iterator,
                                                   final IntFunction<? extends R> mapper) {
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

    public static OfLong longArrayIterator(final long... array) {
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

    public static OfInt longToIntIterator(final OfLong iterator,
                                                            final LongToIntFunction mapper) {
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

    public static OfDouble longToDoubleIterator(final OfLong iterator,
                                                                  final LongToDoubleFunction mapper) {
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

    public static <R> Iterator<R> longToObjIterator(final OfLong iterator,
                                                    final LongFunction<? extends R> mapper) {
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

    public static OfDouble doubleArrayIterator(final double... array) {
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

    public static <T> OfDouble doubleIteratorOf(final Iterator<T> iterator,
                                                                  final ToDoubleFunction<? super T> mapper) {
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

    public static OfDouble doubleTransformingIterator(final OfDouble iterator,
                                                                        final DoubleUnaryOperator mapper) {
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

    public static OfDouble doubleIndexedTransformingIterator(final OfDouble iterator,
                                                                               final DoubleIndexedFunction mapper) {
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

    public static OfInt doubleToIntIterator(final OfDouble doubleIterator,
                                                              final DoubleToIntFunction mapper) {
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

    public static OfLong doubleToLongIterator(final OfDouble doubleIterator,
                                                                final DoubleToLongFunction mapper) {
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

    public static <R> Iterator<R> doubleToObjIterator(final OfDouble iterator,
                                                      final DoubleFunction<? extends R> mapper) {
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
            final OfInt iterator,
            final OfInt otherIterator,
            final IntBinaryOperator merger) {
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
            final OfLong iterator,
            final OfLong otherIterator,
            final LongBinaryOperator merger) {
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
            final OfDouble iterator,
            final OfDouble otherIterator,
            final DoubleBinaryOperator merger) {
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

    public static OfInt emptyIntIterator() {
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

    public static OfLong emptyLongIterator() {
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

    public static OfDouble emptyDoubleIterator() {
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

    public static OfInt distinctIterator(final OfInt iterator) {
        final var observed = IntMutableSet.empty();
        final PrimitiveAtomicIterator.OfInt iteratorX = action -> nextDistinctInt(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctInt(final OfInt iterator,
                                           final IntMutableCollection observed,
                                           final IntConsumer action) {
        while (iterator.hasNext()) {
            final var next = iterator.nextInt();
            if (observed.add(next)) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    public static OfLong distinctIterator(final OfLong iterator) {
        final var observed = LongMutableSet.empty();
        final PrimitiveAtomicIterator.OfLong iteratorX = action -> nextDistinctLong(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctLong(final OfLong iterator,
                                            final LongMutableSet observed,
                                            final LongConsumer action) {
        while (iterator.hasNext()) {
            final var next = iterator.nextLong();
            if (observed.add(next)) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    public static OfDouble distinctIterator(final OfDouble iterator) {
        final var observed = DoubleMutableSet.empty();
        final PrimitiveAtomicIterator.OfDouble iteratorX = action -> nextDistinctDouble(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctDouble(final OfDouble iterator,
                                              final DoubleMutableSet observed,
                                              final DoubleConsumer action) {
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

    public static <A, R> Iterator<R> intGatheringIterator(final PrimitiveIterator.OfInt source, final IntGatherer<A, R> gatherer) {
        final var state = gatherer.initializer().get();
        final var integrator = gatherer.intIntegrator();
        final var finisher = gatherer.finisher();
        final Queue<R> buffer = new ArrayDeque<>();
        return new Iterator<>() {
            boolean finisherCalled = false;
            boolean emitNoMoreItems = false;

            @Override
            public boolean hasNext() {
                if (!buffer.isEmpty()) {
                    return true;
                }
                if (emitNoMoreItems) {
                    return false;
                }
                while (buffer.isEmpty() && source.hasNext()) {
                    if (!integrator.integrate(state, source.nextInt(), buffer::add)) {
                        emitNoMoreItems = true;
                        break;
                    }
                }
                if (!finisherCalled && !source.hasNext()) {
                    finisherCalled = true;
                    finisher.accept(state, buffer::add);
                }
                return !buffer.isEmpty();
            }

            @Override
            public R next() {
                if (hasNext()) {
                    return buffer.remove();
                }
                throw new NoSuchElementException();
            }
        };
    }
}
