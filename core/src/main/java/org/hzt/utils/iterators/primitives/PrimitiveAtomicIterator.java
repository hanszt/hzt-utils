package org.hzt.utils.iterators.primitives;

import org.hzt.utils.iterators.functional_iterator.AtomicIterator;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

@SuppressWarnings({"squid:S2972", "squid:S121", "squid:S119"})
public interface PrimitiveAtomicIterator<T, T_CONS> extends AtomicIterator<T> {

    void forEachRemaining(T_CONS consumer);
    final class IteratorLogger {

        private static final System.Logger LOGGER = System.getLogger(PrimitiveAtomicIterator.class.getSimpleName());

        private IteratorLogger() {
        }

        private static void warn(String message) {
            LOGGER.log(System.Logger.Level.WARNING, message);
        }
    }

    @FunctionalInterface
    interface OfInt extends PrimitiveAtomicIterator<Integer, IntConsumer> {

        boolean tryAdvanceInt(IntConsumer action);

        @Override
        default boolean tryAdvance(Consumer<? super Integer> action) {
            IteratorLogger.warn("Use tryAdvanceInt(IntConsumer) instead");
            return tryAdvanceInt(action::accept);
        }

        @Override
        default Spliterator.OfInt asSpliterator() {
            return Spliterators.spliteratorUnknownSize(asIterator(), 0);
        }

        default void forEachRemaining(IntConsumer action) {
            //noinspection StatementWithEmptyBody
            while (tryAdvanceInt(action));
        }

        @Override
        default void forEachRemaining(Consumer<? super Integer> action) {
            IteratorLogger.warn("Use forEachRemaining(IntConsumer) instead");
            PrimitiveAtomicIterator.super.forEachRemaining(action);
        }

        @Override
        default @NotNull PrimitiveIterator.OfInt asIterator() {
            return  new PrimitiveIterator.OfInt() {

                private final AtomicInteger sink = new AtomicInteger();

                @Override
                public boolean hasNext() {
                    return tryAdvanceInt(sink::set);
                }

                @Override
                public int nextInt() {
                    return sink.get();
                }
            };
        }
    }

    @FunctionalInterface
    interface OfLong extends PrimitiveAtomicIterator<Long, LongConsumer> {

        boolean tryAdvanceLong(LongConsumer action);

        @Override
        default boolean tryAdvance(Consumer<? super Long> action) {
            IteratorLogger.warn("Use tryAdvanceLong(LongConsumer) instead");
            return tryAdvanceLong(action::accept);
        }

        @Override
        default Spliterator.OfLong asSpliterator() {
            return Spliterators.spliteratorUnknownSize(asIterator(), 0);
        }

        default void forEachRemaining(LongConsumer action) {
            //noinspection StatementWithEmptyBody
            while (tryAdvanceLong(action));
        }

        @Override
        default void forEachRemaining(Consumer<? super Long> action) {
            IteratorLogger.warn("Use forEachRemaining(LongConsumer) instead");
            PrimitiveAtomicIterator.super.forEachRemaining(action);
        }

        @Override
        default @NotNull PrimitiveIterator.OfLong asIterator() {
            return  new PrimitiveIterator.OfLong() {

                private final AtomicLong sink = new AtomicLong();

                @Override
                public boolean hasNext() {
                    return tryAdvanceLong(sink::set);
                }

                @Override
                public long nextLong() {
                    return sink.get();
                }
            };
        }
    }

    interface OfDouble extends PrimitiveAtomicIterator<Double, DoubleConsumer> {

        boolean tryAdvanceDouble(DoubleConsumer action);

        @Override
        default boolean tryAdvance(Consumer<? super Double> action) {
            IteratorLogger.warn("Use tryAdvanceDouble(DoubleConsumer) instead");
            return tryAdvanceDouble(action::accept);
        }

        @Override
        default Spliterator.OfDouble asSpliterator() {
            return Spliterators.spliteratorUnknownSize(asIterator(), 0);
        }

        default void forEachRemaining(DoubleConsumer action) {
            //noinspection StatementWithEmptyBody
            while (tryAdvanceDouble(action));
        }

        @Override
        default void forEachRemaining(Consumer<? super Double> action) {
            IteratorLogger.warn("Use forEachRemaining(DoubleConsumer) instead");
            PrimitiveAtomicIterator.super.forEachRemaining(action);
        }

        @Override
        default @NotNull PrimitiveIterator.OfDouble asIterator() {
            return  new PrimitiveIterator.OfDouble() {

                private final DoubleHoldingConsumer sink = new DoubleHoldingConsumer();

                @Override
                public boolean hasNext() {
                    return tryAdvanceDouble(sink::set);
                }

                @Override
                public double nextDouble() {
                    return sink.get();
                }
            };
        }
    }

    class DoubleHoldingConsumer {

        private double value = 0;


        public double get() {
            return value;
        }

        public void set(double value) {
            this.value = value;
        }
    }

    static PrimitiveAtomicIterator.OfInt of(PrimitiveIterator.OfInt iterator) {
        return action -> acceptIfHasNext(iterator, action);
    }

    private static boolean acceptIfHasNext(PrimitiveIterator.OfInt iterator, IntConsumer action) {
        boolean hasNext = iterator.hasNext();
        if (hasNext) {
            action.accept(iterator.nextInt());
        }
        return hasNext;
    }

    static PrimitiveAtomicIterator.OfLong of(PrimitiveIterator.OfLong iterator) {
        return action -> acceptIfHasNext(iterator, action);
    }

    private static boolean acceptIfHasNext(PrimitiveIterator.OfLong iterator, LongConsumer action) {
        boolean hasNext = iterator.hasNext();
        if (hasNext) {
            action.accept(iterator.nextLong());
        }
        return hasNext;
    }

    static PrimitiveAtomicIterator.OfDouble of(PrimitiveIterator.OfDouble iterator) {
        return action -> acceptIfHasNext(iterator, action);
    }

    private static boolean acceptIfHasNext(PrimitiveIterator.OfDouble iterator, DoubleConsumer action) {
        boolean hasNext = iterator.hasNext();
        if (hasNext) {
            action.accept(iterator.nextDouble());
        }
        return hasNext;
    }
}
