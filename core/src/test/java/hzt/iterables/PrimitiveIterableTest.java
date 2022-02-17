package hzt.iterables;

import hzt.iterables.primitive.LongIterable;
import hzt.iterators.PrimitiveIterators;
import hzt.ranges.LongRange;
import hzt.sequences.Sequence;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class PrimitiveIterableTest {

    @Test
    void longStreamFromPrimitiveIterable() {
        LongIterable longSequence = LongSequence.of(1, 2, 3, 4, 5).map(l -> l * 3);

        final long count = LongRange.of(longSequence).count();

        System.out.println("count = " + count);

        final LongStream longStream = StreamSupport.longStream(longSequence.spliterator(), false);

        final long[] longs = longStream.toArray();

        It.println(Arrays.toString(longs));

        assertArrayEquals(new long[]{3, 6, 9, 12, 15}, longs);
    }

    @FunctionalInterface
    private interface LongSequence extends LongIterable,
            Numerable<Long>, Collectable<Long>, Reducable<Long>, Groupable<Long>, Stringable<Long>  {

        static LongSequence of(long... ints) {
            return () -> PrimitiveIterators.longArrayIterator(ints);
        }

        default LongSequence map(LongUnaryOperator mapper) {
            return () -> longMapperIterator(longIterator(), mapper);
        }

        default <R> Sequence<R> mapToObj(LongFunction<R> mapper) {
            return () -> longMapperIterator(longIterator(), mapper);
        }

        private PrimitiveIterator.OfLong longMapperIterator(PrimitiveIterator.OfLong longIterator, LongUnaryOperator mapper) {
            return new PrimitiveIterator.OfLong() {
                @Override
                public long nextLong() {
                    return mapper.applyAsLong(longIterator.nextLong());
                }

                @Override
                public boolean hasNext() {
                    return longIterator.hasNext();
                }
            };
        }

        private <R> Iterator<R> longMapperIterator(PrimitiveIterator.OfLong longIterator, LongFunction<R> mapper) {
            return new Iterator<>() {
                @Override
                public R next() {
                    return mapper.apply(longIterator.nextLong());
                }

                @Override
                public boolean hasNext() {
                    return longIterator.hasNext();
                }
            };
        }
    }
}
