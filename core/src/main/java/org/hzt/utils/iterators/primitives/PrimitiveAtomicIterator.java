package org.hzt.utils.iterators.primitives;

import org.hzt.utils.iterators.functional_iterator.AtomicIterator;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
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

        private static void warn(final String message) {
            LOGGER.log(System.Logger.Level.WARNING, message);
        }
    }

    @FunctionalInterface
    interface OfInt extends PrimitiveAtomicIterator<Integer, IntConsumer> {

        boolean tryAdvanceInt(IntConsumer action);

        @Override
        default boolean tryAdvance(final Consumer<? super Integer> action) {
            if (action instanceof final IntConsumer intConsumer) {
                return tryAdvanceInt(intConsumer);
            }
            IteratorLogger.warn("Use tryAdvanceInt(IntConsumer) instead");
            return tryAdvanceInt(action::accept);
        }

        @Override
        default Spliterator.OfInt asSpliterator() {
            return Spliterators.spliteratorUnknownSize(asIterator(), 0);
        }

        default void forEachRemaining(final IntConsumer action) {
            //noinspection StatementWithEmptyBody
            while (tryAdvanceInt(action)) ;
        }

        @Override
        default void forEachRemaining(final Consumer<? super Integer> action) {
            if (action instanceof final IntConsumer intConsumer) {
                forEachRemaining(intConsumer);
                return;
            }
            IteratorLogger.warn("Use forEachRemaining(IntConsumer) instead");
            PrimitiveAtomicIterator.super.forEachRemaining(action);
        }

        @Override
        default PrimitiveIterator.OfInt asIterator() {
            return new PrimitiveIterator.OfInt() {
                boolean hasNext = false;
                int next = 0;

                @Override
                public boolean hasNext() {
                    return hasNext || (hasNext = tryAdvanceInt(i -> next = i));
                }

                @Override
                public int nextInt() {
                    if (hasNext()) {
                        hasNext = false;
                        return next;
                    }
                    throw new NoSuchElementException();
                }
            };
        }
    }

    @FunctionalInterface
    interface OfLong extends PrimitiveAtomicIterator<Long, LongConsumer> {

        boolean tryAdvanceLong(LongConsumer action);

        @Override
        default boolean tryAdvance(final Consumer<? super Long> action) {
            if (action instanceof final LongConsumer longConsumer) {
                return tryAdvanceLong(longConsumer);
            }
            IteratorLogger.warn("Use tryAdvanceLong(LongConsumer) instead");
            return tryAdvanceLong(action::accept);
        }

        @Override
        default Spliterator.OfLong asSpliterator() {
            return Spliterators.spliteratorUnknownSize(asIterator(), 0);
        }

        default void forEachRemaining(final LongConsumer action) {
            //noinspection StatementWithEmptyBody
            while (tryAdvanceLong(action)) ;
        }

        @Override
        default void forEachRemaining(final Consumer<? super Long> action) {
            if (action instanceof final LongConsumer longConsumer) {
                tryAdvanceLong(longConsumer);
                return;
            }
            IteratorLogger.warn("Use forEachRemaining(LongConsumer) instead");
            PrimitiveAtomicIterator.super.forEachRemaining(action);
        }

        @Override
        default PrimitiveIterator.OfLong asIterator() {
            return new PrimitiveIterator.OfLong() {
                boolean hasNext = false;
                long next = 0L;

                @Override
                public boolean hasNext() {
                    return hasNext || (hasNext = tryAdvanceLong(l -> next = l));
                }

                @Override
                public long nextLong() {
                    if (hasNext()) {
                        hasNext = false;
                        return next;
                    }
                    throw new NoSuchElementException();
                }
            };
        }
    }

    interface OfDouble extends PrimitiveAtomicIterator<Double, DoubleConsumer> {

        boolean tryAdvanceDouble(DoubleConsumer action);

        @Override
        default boolean tryAdvance(final Consumer<? super Double> action) {
            if (action instanceof final DoubleConsumer doubleConsumer) {
                return tryAdvanceDouble(doubleConsumer);
            }
            IteratorLogger.warn("Use tryAdvanceDouble(DoubleConsumer) instead");
            return tryAdvanceDouble(action::accept);
        }

        @Override
        default Spliterator.OfDouble asSpliterator() {
            return Spliterators.spliteratorUnknownSize(asIterator(), 0);
        }

        default void forEachRemaining(final DoubleConsumer action) {
            //noinspection StatementWithEmptyBody
            while (tryAdvanceDouble(action)) ;
        }

        @Override
        default void forEachRemaining(final Consumer<? super Double> action) {
            if (action instanceof final DoubleConsumer doubleConsumer) {
                tryAdvanceDouble(doubleConsumer);
                return;
            }
            IteratorLogger.warn("Use forEachRemaining(DoubleConsumer) instead");
            PrimitiveAtomicIterator.super.forEachRemaining(action);
        }

        @Override
        default PrimitiveIterator.OfDouble asIterator() {
            return new PrimitiveIterator.OfDouble() {
                boolean hasNext = false;
                double next = 0.0;

                @Override
                public boolean hasNext() {
                    return hasNext || (hasNext = tryAdvanceDouble(d -> next = d));
                }

                @Override
                public double nextDouble() {
                    if (hasNext()) {
                        hasNext = false;
                        return next;
                    }
                    throw new NoSuchElementException();
                }
            };
        }
    }

    static PrimitiveAtomicIterator.OfInt of(final PrimitiveIterator.OfInt iterator) {
        return action -> acceptIfHasNext(iterator, action);
    }

    static PrimitiveAtomicIterator.OfInt of(final Spliterator.OfInt spliterator) {
        return spliterator::tryAdvance;
    }

    private static boolean acceptIfHasNext(final PrimitiveIterator.OfInt iterator, final IntConsumer action) {
        final var hasNext = iterator.hasNext();
        if (hasNext) {
            action.accept(iterator.nextInt());
        }
        return hasNext;
    }

    static PrimitiveAtomicIterator.OfLong of(final PrimitiveIterator.OfLong iterator) {
        return action -> acceptIfHasNext(iterator, action);
    }

    static PrimitiveAtomicIterator.OfLong of(final Spliterator.OfLong spliterator) {
        return spliterator::tryAdvance;
    }

    private static boolean acceptIfHasNext(final PrimitiveIterator.OfLong iterator, final LongConsumer action) {
        final var hasNext = iterator.hasNext();
        if (hasNext) {
            action.accept(iterator.nextLong());
        }
        return hasNext;
    }

    static PrimitiveAtomicIterator.OfDouble of(final PrimitiveIterator.OfDouble iterator) {
        return action -> acceptIfHasNext(iterator, action);
    }

    static PrimitiveAtomicIterator.OfDouble of(final Spliterator.OfDouble spliterator) {
        return spliterator::tryAdvance;
    }

    private static boolean acceptIfHasNext(final PrimitiveIterator.OfDouble iterator, final DoubleConsumer action) {
        final var hasNext = iterator.hasNext();
        if (hasNext) {
            action.accept(iterator.nextDouble());
        }
        return hasNext;
    }
}
