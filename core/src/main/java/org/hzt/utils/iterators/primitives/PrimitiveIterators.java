package org.hzt.utils.iterators.primitives;

import org.hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleFunction;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongFunction;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public final class PrimitiveIterators {

    private PrimitiveIterators() {
    }

    @NotNull
    public static PrimitiveIterator.OfInt intArrayIterator(int[] array) {
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
    public static <T> PrimitiveIterator.OfInt intIteratorOf(Iterator<T> iterator, ToIntFunction<? super T> mapper) {
        return new PrimitiveIterator.OfInt() {
            @Override
            public int nextInt() {
                if (iterator instanceof PrimitiveIterator.OfInt intIterator) {
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

    @NotNull
    public static PrimitiveIterator.OfInt intTransformingIterator(PrimitiveIterator.OfInt iterator, IntUnaryOperator mapper) {
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
    public static PrimitiveIterator.OfLong intToLongIterator(PrimitiveIterator.OfInt iterator, IntToLongFunction mapper) {
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
    public static PrimitiveIterator.OfDouble intToDoubleIterator(PrimitiveIterator.OfInt iterator, IntToDoubleFunction mapper) {
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

    public static <R> Iterator<R> intToObjIterator(PrimitiveIterator.OfInt iterator, IntFunction<R> mapper) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public R next() {
                return mapper.apply(iterator.nextInt());
            }
        };
    }

    @NotNull
    public static PrimitiveIterator.OfLong longArrayIterator(long[] array) {
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
                if (iterator instanceof PrimitiveIterator.OfLong longIterator) {
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
    public static PrimitiveIterator.OfInt longToIntIterator(PrimitiveIterator.OfLong iterator, LongToIntFunction mapper) {
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
    public static PrimitiveIterator.OfDouble longToDoubleIterator(PrimitiveIterator.OfLong iterator, LongToDoubleFunction mapper) {
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

    public static <R> Iterator<R> longToObjIterator(PrimitiveIterator.OfLong iterator, LongFunction<R> mapper) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public R next() {
                return mapper.apply(iterator.nextLong());
            }
        };
    }

    @NotNull
    public static PrimitiveIterator.OfDouble doubleArrayIterator(double[] array) {
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
    public static <T> PrimitiveIterator.OfDouble doubleIteratorOf(Iterator<T> iterator, ToDoubleFunction<? super T> mapper) {
        return new PrimitiveIterator.OfDouble() {
            @Override
            public double nextDouble() {
                if (iterator instanceof PrimitiveIterator.OfDouble doubleIterator) {
                    return doubleIterator.nextDouble();
                } else if (iterator instanceof PrimitiveIterator.OfInt intIterator) {
                    return intToDoubleIterator(intIterator, It::asDouble).nextDouble();
                } else if (iterator instanceof PrimitiveIterator.OfLong longIterator) {
                    return longToDoubleIterator(longIterator, It::asDouble).nextDouble();
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
    public static PrimitiveIterator.OfDouble doubleTransformingIterator(PrimitiveIterator.OfDouble iterator, DoubleUnaryOperator mapper) {
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

    public static PrimitiveIterator.OfInt doubleToIntIterator(PrimitiveIterator.OfDouble doubleIterator, DoubleToIntFunction mapper) {
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

    public static PrimitiveIterator.OfLong doubleToLongIterator(PrimitiveIterator.OfDouble doubleIterator, DoubleToLongFunction mapper) {
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

    public static <R> Iterator<R> doubleToObjIterator(PrimitiveIterator.OfDouble iterator, DoubleFunction<R> mapper) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
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

    public static PrimitiveIterator.@NotNull OfInt emptyIntIterator() {
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

    public static PrimitiveIterator.@NotNull OfLong emptyLongIterator() {
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

    public static PrimitiveIterator.@NotNull OfDouble emptyDoubleIterator() {
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
    private static String getMessage(int index) {
        return "index out of bounds. (Index value: " + index + ")";
    }
}
