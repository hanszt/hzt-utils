package org.hzt.utils.iterators.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.primitives.DoubleMutableSet;
import org.hzt.utils.collections.primitives.IntMutableCollection;
import org.hzt.utils.collections.primitives.IntMutableSet;
import org.hzt.utils.collections.primitives.LongMutableSet;
import org.hzt.utils.function.primitives.DoubleIndexedFunction;
import org.hzt.utils.function.primitives.LongIndexedFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
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

@SuppressWarnings("squid:S1200")
public final class PrimitiveIterators {

    private static final String ITERATOR_INDEX_OVERFLOW = "Iterator index overflow";

    private PrimitiveIterators() {
    }

    @NotNull
    public static PrimitiveIterator.OfInt intArrayIterator(int @NotNull ... array) {
        return new PrimitiveIterator.OfInt() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public int nextInt() {
                int prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException(getMessage(index));
                }
                return array[index++];
            }
        };
    }

    @NotNull
    public static <T> PrimitiveIterator.OfInt intIteratorOf(@NotNull Iterator<T> iterator,
                                                            @NotNull ToIntFunction<? super T> mapper) {
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

    public static <T> PrimitiveIterator.OfInt toIntFlatMappingIterator(@NotNull Iterator<T> iterator,
                                                                       @NotNull Function<? super T,
                                                                               ? extends PrimitiveIterator.OfInt> mapper) {
        return new ToIntFlatMappingIterator<>(iterator, mapper);
    }

    public static <T> PrimitiveIterator.OfLong toLongFlatMappingIterator(@NotNull Iterator<T> iterator,
                                                                         @NotNull Function<? super T,
                                                                                 ? extends PrimitiveIterator.OfLong> mapper) {
        return new ToLongFlatMappingIterator<>(iterator, mapper);
    }

    public static <T> PrimitiveIterator.OfDouble toDoubleFlatMappingIterator(@NotNull Iterator<T> iterator,
                                                                             @NotNull Function<? super T,
                                                                                     ? extends PrimitiveIterator.OfDouble> mapper) {
        return new ToDoubleFlatMappingIterator<>(iterator, mapper);
    }

    @NotNull
    public static PrimitiveIterator.OfInt intTransformingIterator(@NotNull PrimitiveIterator.OfInt iterator,
                                                                  @NotNull IntUnaryOperator mapper) {
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

    @NotNull
    public static PrimitiveIterator.OfInt intIndexedTransformingIterator(PrimitiveIterator.OfInt iterator, IntBinaryOperator mapper) {
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

    @NotNull
    public static PrimitiveIterator.OfLong intToLongIterator(@NotNull PrimitiveIterator.OfInt iterator,
                                                             @NotNull IntToLongFunction mapper) {
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

    @NotNull
    public static PrimitiveIterator.OfDouble intToDoubleIterator(@NotNull PrimitiveIterator.OfInt iterator,
                                                                 @NotNull IntToDoubleFunction mapper) {
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

    public static <R> Iterator<R> intToObjIterator(@NotNull PrimitiveIterator.OfInt iterator,
                                                   @NotNull IntFunction<? extends R> mapper) {
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

    @NotNull
    public static PrimitiveIterator.OfLong longArrayIterator(long @NotNull ... array) {
        return new PrimitiveIterator.OfLong() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public long nextLong() {
                int prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException(getMessage(index));
                }
                return array[index++];
            }
        };
    }

    @NotNull
    public static <T> PrimitiveIterator.OfLong longIteratorOf(Iterator<T> iterator, ToLongFunction<? super T> mapper) {
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

    @NotNull
    public static PrimitiveIterator.OfLong longTransformingIterator(PrimitiveIterator.OfLong iterator, LongUnaryOperator mapper) {
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

    @NotNull
    public static PrimitiveIterator.OfLong longIndexedTransformingIterator(PrimitiveIterator.OfLong iterator, LongIndexedFunction mapper) {
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

    @NotNull
    public static PrimitiveIterator.OfInt longToIntIterator(@NotNull PrimitiveIterator.OfLong iterator,
                                                            @NotNull LongToIntFunction mapper) {
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

    @NotNull
    public static PrimitiveIterator.OfDouble longToDoubleIterator(@NotNull PrimitiveIterator.OfLong iterator,
                                                                  @NotNull LongToDoubleFunction mapper) {
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

    public static <R> Iterator<R> longToObjIterator(@NotNull PrimitiveIterator.OfLong iterator,
                                                    @NotNull LongFunction<? extends R> mapper) {
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

    @NotNull
    public static PrimitiveIterator.OfDouble doubleArrayIterator(double @NotNull ... array) {
        return new PrimitiveIterator.OfDouble() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public double nextDouble() {
                int prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException(getMessage(index));
                }
                return array[index++];
            }
        };
    }

    @NotNull
    public static <T> PrimitiveIterator.OfDouble doubleIteratorOf(@NotNull Iterator<T> iterator,
                                                                  @NotNull ToDoubleFunction<? super T> mapper) {
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

    @NotNull
    public static PrimitiveIterator.OfDouble doubleTransformingIterator(@NotNull PrimitiveIterator.OfDouble iterator,
                                                                        @NotNull DoubleUnaryOperator mapper) {
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

    @NotNull
    public static PrimitiveIterator.OfDouble doubleIndexedTransformingIterator(@NotNull PrimitiveIterator.OfDouble iterator,
                                                                               @NotNull DoubleIndexedFunction mapper) {
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

    public static PrimitiveIterator.OfInt doubleToIntIterator(@NotNull PrimitiveIterator.OfDouble doubleIterator,
                                                              @NotNull DoubleToIntFunction mapper) {
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

    public static PrimitiveIterator.OfLong doubleToLongIterator(@NotNull PrimitiveIterator.OfDouble doubleIterator,
                                                                @NotNull DoubleToLongFunction mapper) {
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

    public static <R> Iterator<R> doubleToObjIterator(@NotNull PrimitiveIterator.OfDouble iterator,
                                                      @NotNull DoubleFunction<? extends R> mapper) {
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
            @NotNull PrimitiveIterator.OfInt iterator,
            @NotNull PrimitiveIterator.OfInt otherIterator,
            @NotNull IntBinaryOperator merger) {
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
            @NotNull PrimitiveIterator.OfLong iterator,
            @NotNull PrimitiveIterator.OfLong otherIterator,
            @NotNull LongBinaryOperator merger) {
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
            @NotNull PrimitiveIterator.OfDouble iterator,
            @NotNull PrimitiveIterator.OfDouble otherIterator,
            @NotNull DoubleBinaryOperator merger) {
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

    public static @NotNull PrimitiveIterator.OfInt emptyIntIterator() {
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

    public static @NotNull PrimitiveIterator.OfLong emptyLongIterator() {
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

    public static @NotNull PrimitiveIterator.OfDouble emptyDoubleIterator() {
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

    @NotNull
    public static PrimitiveIterator.OfInt distinctIterator(@NotNull PrimitiveIterator.OfInt iterator) {
        final IntMutableSet observed = IntMutableSet.empty();
        final PrimitiveAtomicIterator.OfInt iteratorX = action -> nextDistinctInt(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctInt(@NotNull PrimitiveIterator.OfInt iterator,
                                           @NotNull IntMutableCollection observed,
                                           @NotNull IntConsumer action) {
        while (iterator.hasNext()) {
            int next = iterator.nextInt();
            if (observed.add(next)) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    @NotNull
    public static PrimitiveIterator.OfLong distinctIterator(@NotNull PrimitiveIterator.OfLong iterator) {
        final LongMutableSet observed = LongMutableSet.empty();
        final PrimitiveAtomicIterator.OfLong iteratorX = action -> nextDistinctLong(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctLong(@NotNull PrimitiveIterator.OfLong iterator,
                                            @NotNull LongMutableSet observed,
                                            @NotNull LongConsumer action) {
        while (iterator.hasNext()) {
            long next = iterator.nextLong();
            if (observed.add(next)) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    @NotNull
    public static PrimitiveIterator.OfDouble distinctIterator(@NotNull PrimitiveIterator.OfDouble iterator) {
        final DoubleMutableSet observed = DoubleMutableSet.empty();
        final PrimitiveAtomicIterator.OfDouble iteratorX = action -> nextDistinctDouble(iterator, observed, action);
        return iteratorX.asIterator();
    }

    private static boolean nextDistinctDouble(@NotNull PrimitiveIterator.OfDouble iterator,
                                              @NotNull DoubleMutableSet observed,
                                              @NotNull DoubleConsumer action) {
        while (iterator.hasNext()) {
            double next = iterator.nextDouble();
            if (observed.add(next)) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }

    @NotNull
    private static String getMessage(int index) {
        return "index out of bounds. (Index value: " + index + ")";
    }
}
