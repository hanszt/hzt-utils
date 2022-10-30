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

        private IteratorLogger() {
        }

        @SuppressWarnings("squid:S106")
        private static void warn(String message) {
            System.err.println(message);
        }
    }

    @FunctionalInterface
    interface OfInt extends PrimitiveAtomicIterator<Integer, IntConsumer> {

        boolean tryAdvanceInt(IntConsumer action);

        @Override
        default boolean tryAdvance(Consumer<? super Integer> action) {
            if (action instanceof IntConsumer) {
                return tryAdvanceInt((IntConsumer) action);
            }
            IteratorLogger.warn("Use tryAdvanceInt(IntConsumer) instead");
            return tryAdvanceInt(action::accept);
        }

        @Override
        default Spliterator.OfInt asSpliterator() {
            return Spliterators.spliteratorUnknownSize(asIterator(), 0);
        }

        default void forEachRemaining(IntConsumer action) {
            //noinspection StatementWithEmptyBody
            while (tryAdvanceInt(action)) {
            }
        }

        @Override
        default void forEachRemaining(Consumer<? super Integer> action) {
            if (action instanceof IntConsumer) {
                forEachRemaining((IntConsumer) action);
                return;
            }
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
            if (action instanceof IntConsumer) {
                return tryAdvanceLong((LongConsumer) action);
            }
            IteratorLogger.warn("Use tryAdvanceLong(LongConsumer) instead");
            return tryAdvanceLong(action::accept);
        }

        @Override
        default Spliterator.OfLong asSpliterator() {
            return Spliterators.spliteratorUnknownSize(asIterator(), 0);
        }

        default void forEachRemaining(LongConsumer action) {
            //noinspection StatementWithEmptyBody
            while (tryAdvanceLong(action)) {
            }
        }

        @Override
        default void forEachRemaining(Consumer<? super Long> action) {
            if (action instanceof LongConsumer) {
                tryAdvanceLong((LongConsumer) action);
                return;
            }
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
            if (action instanceof DoubleConsumer) {
                return tryAdvanceDouble((DoubleConsumer) action);
            }
            IteratorLogger.warn("Use tryAdvanceDouble(DoubleConsumer) instead");
            return tryAdvanceDouble(action::accept);
        }

        @Override
        default Spliterator.OfDouble asSpliterator() {
            return Spliterators.spliteratorUnknownSize(asIterator(), 0);
        }

        default void forEachRemaining(DoubleConsumer action) {
            //noinspection StatementWithEmptyBody
            while (tryAdvanceDouble(action)) {
            }
        }

        @Override
        default void forEachRemaining(Consumer<? super Double> action) {
            if (action instanceof DoubleConsumer) {
                tryAdvanceDouble((DoubleConsumer) action);
                return;
            }
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
        return action -> {
            boolean hasNext = iterator.hasNext();
            if (hasNext) {
                action.accept(iterator.nextInt());
            }
            return hasNext;
        };
    }

    static PrimitiveAtomicIterator.OfInt of(Spliterator.OfInt spliterator) {
        return spliterator::tryAdvance;
    }

    static PrimitiveAtomicIterator.OfLong of(PrimitiveIterator.OfLong iterator) {
        return action -> {
            boolean hasNext = iterator.hasNext();
            if (hasNext) {
                action.accept(iterator.nextLong());
            }
            return hasNext;
        };
    }

    static PrimitiveAtomicIterator.OfLong of(Spliterator.OfLong spliterator) {
        return spliterator::tryAdvance;
    }

    static PrimitiveAtomicIterator.OfDouble of(PrimitiveIterator.OfDouble iterator) {
        return action -> {
            boolean hasNext = iterator.hasNext();
            if (hasNext) {
                action.accept(iterator.nextDouble());
            }
            return hasNext;
        };
    }

    static PrimitiveAtomicIterator.OfDouble of(Spliterator.OfDouble spliterator) {
        return spliterator::tryAdvance;
    }
}
