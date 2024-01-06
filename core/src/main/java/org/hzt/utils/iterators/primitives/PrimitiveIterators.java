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
import org.hzt.utils.gatherers.Gatherer;
import org.hzt.utils.gatherers.primitives.IntGatherer;
import org.hzt.utils.gatherers.primitives.IntIntegrator;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Queue;
import java.util.function.BiConsumer;
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

    public static PrimitiveIterator.OfInt intArrayIterator(final int... array) {
        return new PrimitiveIterator.OfInt() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public int nextInt() {
                final int prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException(getMessage(index));
                }
                return array[index++];
            }
        };
    }

    public static <T> PrimitiveIterator.OfInt intIteratorOf(final Iterator<T> iterator,
                                                            final ToIntFunction<? super T> mapper) {
        return new PrimitiveIterator.OfInt() {
            @Override
            public int nextInt() {
                if (iterator instanceof PrimitiveIterator.OfInt) {
                    return ((PrimitiveIterator.OfInt) iterator).nextInt();
                }
                return mapper.applyAsInt(iterator.next());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    public static <T> PrimitiveIterator.OfInt toIntFlatMappingIterator(final Iterator<T> iterator,
                                                                       final Function<? super T,
                                                                               ? extends PrimitiveIterator.OfInt> mapper) {
        return new ToIntFlatMappingIterator<>(iterator, mapper);
    }

    public static <T> PrimitiveIterator.OfLong toLongFlatMappingIterator(final Iterator<T> iterator,
                                                                         final Function<? super T,
                                                                                 ? extends PrimitiveIterator.OfLong> mapper) {
        return new ToLongFlatMappingIterator<>(iterator, mapper);
    }

    public static <T> PrimitiveIterator.OfDouble toDoubleFlatMappingIterator(final Iterator<T> iterator,
                                                                             final Function<? super T,
                                                                                     ? extends PrimitiveIterator.OfDouble> mapper) {
        return new ToDoubleFlatMappingIterator<>(iterator, mapper);
    }

    public static PrimitiveIterator.OfInt intTransformingIterator(final PrimitiveIterator.OfInt iterator,
                                                                  final IntUnaryOperator mapper) {
        return new PrimitiveIterator.OfInt() {
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

    public static PrimitiveIterator.OfInt intIndexedTransformingIterator(final PrimitiveIterator.OfInt iterator, final IntBinaryOperator mapper) {
        return new PrimitiveIterator.OfInt() {

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

    public static PrimitiveIterator.OfLong intToLongIterator(final PrimitiveIterator.OfInt iterator,
                                                             final IntToLongFunction mapper) {
        return new PrimitiveIterator.OfLong() {
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

    public static PrimitiveIterator.OfDouble intToDoubleIterator(final PrimitiveIterator.OfInt iterator,
                                                                 final IntToDoubleFunction mapper) {
        return new PrimitiveIterator.OfDouble() {
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

    public static <R> Iterator<R> intToObjIterator(final PrimitiveIterator.OfInt iterator,
                                                   final IntFunction<? extends R> mapper) {
        return new Iterator<R>() {
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

    public static PrimitiveIterator.OfLong longArrayIterator(final long... array) {
        return new PrimitiveIterator.OfLong() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public long nextLong() {
                final int prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException(getMessage(index));
                }
                return array[index++];
            }
        };
    }

    public static <T> PrimitiveIterator.OfLong longIteratorOf(final Iterator<T> iterator, final ToLongFunction<? super T> mapper) {
        return new PrimitiveIterator.OfLong() {
            @Override
            public long nextLong() {
                if (iterator instanceof PrimitiveIterator.OfLong) {
                    return ((PrimitiveIterator.OfLong) iterator).nextLong();
                }
                return mapper.applyAsLong(iterator.next());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    public static PrimitiveIterator.OfLong longTransformingIterator(final PrimitiveIterator.OfLong iterator, final LongUnaryOperator mapper) {
        return new PrimitiveIterator.OfLong() {
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

    public static PrimitiveIterator.OfLong longIndexedTransformingIterator(final PrimitiveIterator.OfLong iterator, final LongIndexedFunction mapper) {
        return new PrimitiveIterator.OfLong() {

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

    public static PrimitiveIterator.OfInt longToIntIterator(final PrimitiveIterator.OfLong iterator,
                                                            final LongToIntFunction mapper) {
        return new PrimitiveIterator.OfInt() {
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

    public static PrimitiveIterator.OfDouble longToDoubleIterator(final PrimitiveIterator.OfLong iterator,
                                                                  final LongToDoubleFunction mapper) {
        return new PrimitiveIterator.OfDouble() {
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

    public static <R> Iterator<R> longToObjIterator(final PrimitiveIterator.OfLong iterator,
                                                    final LongFunction<? extends R> mapper) {
        return new Iterator<R>() {
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

    public static PrimitiveIterator.OfDouble doubleArrayIterator(final double... array) {
        return new PrimitiveIterator.OfDouble() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public double nextDouble() {
                final int prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException(getMessage(index));
                }
                return array[index++];
            }
        };
    }

    public static <T> PrimitiveIterator.OfDouble doubleIteratorOf(final Iterator<T> iterator,
                                                                  final ToDoubleFunction<? super T> mapper) {
        return new PrimitiveIterator.OfDouble() {
            @Override
            public double nextDouble() {
                if (iterator instanceof PrimitiveIterator.OfDouble) {
                    return ((PrimitiveIterator.OfDouble) iterator).nextDouble();
                } else if (iterator instanceof PrimitiveIterator.OfInt) {
                    return intToDoubleIterator((PrimitiveIterator.OfInt) iterator, It::asDouble).nextDouble();
                } else if (iterator instanceof PrimitiveIterator.OfLong) {
                    return longToDoubleIterator((PrimitiveIterator.OfLong) iterator, It::asDouble).nextDouble();
                } else {
                    return mapper.applyAsDouble(iterator.next());
                }
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    public static PrimitiveIterator.OfDouble doubleTransformingIterator(final PrimitiveIterator.OfDouble iterator,
                                                                        final DoubleUnaryOperator mapper) {
        return new PrimitiveIterator.OfDouble() {
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

    public static PrimitiveIterator.OfDouble doubleIndexedTransformingIterator(final PrimitiveIterator.OfDouble iterator,
                                                                               final DoubleIndexedFunction mapper) {
        return new PrimitiveIterator.OfDouble() {

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

    public static PrimitiveIterator.OfInt doubleToIntIterator(final PrimitiveIterator.OfDouble doubleIterator,
                                                              final DoubleToIntFunction mapper) {
        return new PrimitiveIterator.OfInt() {
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

    public static PrimitiveIterator.OfLong doubleToLongIterator(final PrimitiveIterator.OfDouble doubleIterator,
                                                                final DoubleToLongFunction mapper) {
        return new PrimitiveIterator.OfLong() {
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

    public static <R> Iterator<R> doubleToObjIterator(final PrimitiveIterator.OfDouble iterator,
                                                      final DoubleFunction<? extends R> mapper) {
        return new Iterator<R>() {
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

    public static PrimitiveIterator.OfInt mergingIterator(
            final PrimitiveIterator.OfInt iterator,
            final PrimitiveIterator.OfInt otherIterator,
            final IntBinaryOperator merger) {
        return new PrimitiveIterator.OfInt() {
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

    public static PrimitiveIterator.OfLong mergingIterator(
            final PrimitiveIterator.OfLong iterator,
            final PrimitiveIterator.OfLong otherIterator,
            final LongBinaryOperator merger) {
        return new PrimitiveIterator.OfLong() {
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

    public static PrimitiveIterator.OfDouble mergingIterator(
            final PrimitiveIterator.OfDouble iterator,
            final PrimitiveIterator.OfDouble otherIterator,
            final DoubleBinaryOperator merger) {
        return new PrimitiveIterator.OfDouble() {
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

    public static PrimitiveIterator.OfInt emptyIntIterator() {
        return new PrimitiveIterator.OfInt() {
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

    public static PrimitiveIterator.OfLong emptyLongIterator() {
        return new PrimitiveIterator.OfLong() {
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

    public static PrimitiveIterator.OfDouble emptyDoubleIterator() {
        return new PrimitiveIterator.OfDouble() {
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

    public static PrimitiveIterator.OfInt distinctIterator(final PrimitiveIterator.OfInt iterator) {
        final IntMutableSet observed = IntMutableSet.empty();
        final PrimitiveAtomicIterator.OfInt iteratorX = action -> nextDistinctInt(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctInt(final PrimitiveIterator.OfInt iterator,
                                           final IntMutableCollection observed,
                                           final IntConsumer action) {
        while (iterator.hasNext()) {
            final int next = iterator.nextInt();
            if (observed.add(next)) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    public static PrimitiveIterator.OfLong distinctIterator(final PrimitiveIterator.OfLong iterator) {
        final LongMutableSet observed = LongMutableSet.empty();
        final PrimitiveAtomicIterator.OfLong iteratorX = action -> nextDistinctLong(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctLong(final PrimitiveIterator.OfLong iterator,
                                            final LongMutableSet observed,
                                            final LongConsumer action) {
        while (iterator.hasNext()) {
            final long next = iterator.nextLong();
            if (observed.add(next)) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    public static PrimitiveIterator.OfDouble distinctIterator(final PrimitiveIterator.OfDouble iterator) {
        final DoubleMutableSet observed = DoubleMutableSet.empty();
        final PrimitiveAtomicIterator.OfDouble iteratorX = action -> nextDistinctDouble(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctDouble(final PrimitiveIterator.OfDouble iterator,
                                              final DoubleMutableSet observed,
                                              final DoubleConsumer action) {
        while (iterator.hasNext()) {
            final double next = iterator.nextDouble();
            if (observed.add(next)) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    public static PrimitiveIterator.OfInt intScanningIterator(final PrimitiveIterator.OfInt iterator,
                                                              final int initial, final IntBinaryOperator operation) {
        return new IntScanningIterator(iterator, initial, operation);
    }

    public static PrimitiveIterator.OfLong longScanningIterator(final PrimitiveIterator.OfLong iterator,
                                                                final long initial, final LongBinaryOperator operation) {
        return new LongScanningIterator(iterator, initial, operation);
    }

    public static PrimitiveIterator.OfDouble doubleScanningIterator(final PrimitiveIterator.OfDouble iterator,
                                                                    final double initial, final DoubleBinaryOperator operation) {
        return new DoubleScanningIterator(iterator, initial, operation);
    }

    private static String getMessage(final int index) {
        return "index out of bounds. (Index value: " + index + ")";
    }

    public static PrimitiveIterator.OfInt reverseIterator(final IntList doubleList) {
        final PrimitiveListIterator.OfInt listIterator = doubleList.listIterator(doubleList.size());
        return new PrimitiveIterator.OfInt() {
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

    public static PrimitiveIterator.OfLong reverseIterator(final LongList doubleList) {
        final PrimitiveListIterator.OfLong listIterator = doubleList.listIterator(doubleList.size());
        return new PrimitiveIterator.OfLong() {

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

    public static PrimitiveIterator.OfDouble reverseIterator(final DoubleList doubleList) {
        final PrimitiveListIterator.OfDouble listIterator = doubleList.listIterator(doubleList.size());
        return new PrimitiveIterator.OfDouble() {

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
        final A state = gatherer.initializer().get();
        final IntIntegrator<A, R> integrator = gatherer.intIntegrator();
        final BiConsumer<A, Gatherer.Downstream<? super R>> finisher = gatherer.finisher();
        final Queue<R> buffer = new ArrayDeque<>();
        return new Iterator<R>() {
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
