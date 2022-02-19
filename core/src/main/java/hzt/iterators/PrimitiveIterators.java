package hzt.iterators;

import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
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
    public static <T> PrimitiveIterator.OfInt intIteratorOf(Iterator<T> iterator, ToIntFunction<T> mapper) {
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
    public static <T> PrimitiveIterator.OfLong longIteratorOf(Iterator<T> iterator, ToLongFunction<T> mapper) {
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
    public static <T> PrimitiveIterator.OfDouble doubleIteratorOf(Iterator<T> iterator, ToDoubleFunction<T> mapper) {
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

    @NotNull
    private static String getMessage(int index) {
        return "index out of bounds. (Index value: " + index + ")";
    }
}
