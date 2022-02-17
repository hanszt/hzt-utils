package hzt.iterators;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
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
                return mapper.applyAsInt(iterator.next());
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
                return mapper.applyAsLong(iterator.next());
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
                return mapper.applyAsDouble(iterator.next());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    @NotNull
    public static Iterator<Boolean> booleanArrayIterator(boolean[] array) {
        return new Iterator<>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < array.length;
            }
            @Override
            public Boolean next() {
                int prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException(getMessage(index));
                }
                return array[index++];
            }
        };
    }

    @NotNull
    private static String getMessage(int index) {
        return "index out of bounds. (Index value: " + index + ")";
    }
}
