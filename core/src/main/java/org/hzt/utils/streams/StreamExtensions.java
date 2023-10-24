package org.hzt.utils.streams;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.spined_buffers.SpinedBuffer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Spliterators;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Spliterator.ORDERED;
import static java.util.stream.StreamSupport.stream;

public final class StreamExtensions {

    private StreamExtensions() {
    }

    public static <T, R> StreamExtension<T, R> map(Function<? super T, ? extends R> mapper) {
        return tStream -> {
            final var spliterator = tStream.spliterator();
            return stream(new Spliterators.AbstractSpliterator<R>(spliterator.estimateSize(), spliterator.characteristics()) {

                private final AtomicReference<T> atomicReference = new AtomicReference<>();

                @Override
                public boolean tryAdvance(Consumer<? super R> action) {
                    final var hasNext = spliterator.tryAdvance(atomicReference::set);
                    action.accept(mapper.apply(atomicReference.get()));
                    return hasNext;
                }
            }, tStream.isParallel());
        };
    }

    public static <T> StreamExtension<T, T> peek(Consumer<? super T> consumer) {
        return map(t -> {
                    consumer.accept(t);
                    return t;
                }
        );
    }

    /**
     * Experimental mapper
     *
     * @param maxConcurrency max concurrency
     * @param mapper         the mapper function
     * @param <T>            the type of the input stream
     * @param <R>            the type of the output stream
     * @return a StreamExtension to use as extension for a stream pipeline
     */
    public static <T, R> StreamExtension<T, R> mapConcurrent(final int maxConcurrency, final Function<? super T, ? extends R> mapper) {
        if (maxConcurrency <= 0) {
            throw new IllegalArgumentException("'maxConcurrency' needs to be greater than 0");
        }
        Objects.requireNonNull(mapper, "'mapper' must not be null");
        @FunctionalInterface
        interface Downstream<R> {

            /**
             * @param element the element to push to the downstream
             * @return true if more elements are desired and false if not.
             */
            boolean push(R element);
        }
        class State {
            final Deque<Future<R>> window = new ArrayDeque<>(maxConcurrency);
            final Semaphore windowLock = new Semaphore(maxConcurrency);


            final boolean integrate(T t, Downstream<? super R> downstream) {
                windowLock.acquireUninterruptibly();

                var task = new FutureTask<R>(() -> {
                    try {
                        return mapper.apply(t);
                    } finally {
                        windowLock.release();
                    }
                });
                Thread.startVirtualThread(task);
                if (!window.add(task)) {
                    throw new IllegalStateException("Unable to add task even though cleared to do so");
                }
                return flush(0, downstream);
            }

            final boolean flush(final long n, Downstream<? super R> downstream) {
                var atLeastN = n;
                boolean proceed = true;
                try {
                    Future<R> current;
                    while (proceed && (current = window.peek()) != null && (current.isDone() || atLeastN > 0)) {
                        proceed = downstream.push(current.get());
                        atLeastN -= 1;
                        // This should logically never be true
                        if (window.pop() != current) {
                            throw new IllegalStateException("current isn't the head of the queue");
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    Thread.currentThread().interrupt();
                    proceed = false; // Ensure cleanup
                    throw new IllegalStateException(e);
                } finally {
                    // Cancel outstanding tasks if no more elements are to be produced
                    if (!proceed) {
                        Future<R> next;
                        while ((next = window.pollFirst()) != null) {
                            next.cancel(true);
                        }
                    }
                }
                return proceed;
            }
        }
        return tStream -> {
            final var source = tStream.iterator();
            final var spinedBuffer = new SpinedBuffer<R>();
            final Downstream<R> downStream = (R r) -> {
                spinedBuffer.accept(r);
                return true;
            };
            final var state = new State();
            //noinspection StatementWithEmptyBody
            while (source.hasNext() && state.integrate(source.next(), downStream)) ;
            state.flush(Long.MAX_VALUE, downStream);
            return stream(spinedBuffer.spliterator(), false);
        };
    }

    public static <T> StreamExtension<T, List<T>> chunked(int size) {
        return windowed(size, size, true);
    }

    public static <T> StreamExtension<T, List<T>> windowed(int size) {
        return windowed(size, 1);
    }

    public static <T, R> StreamExtension<T, R> windowed(int size, Function<List<T>, R> mapper) {
        return windowed(size, 1, false, mapper);
    }

    public static <T> StreamExtension<T, List<T>> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    public static <T> StreamExtension<T, List<T>> windowed(int size,
                                                           int step,
                                                           boolean partialWindows) {
        return windowed(size, step, partialWindows, s -> s);
    }

    public static <T, R> StreamExtension<T, R> windowed(int size,
                                                        int step,
                                                        boolean partialWindows,
                                                        Function<List<T>, R> mapper) {
        return stream -> stream(() -> Spliterators.spliteratorUnknownSize(Sequence.of(stream::iterator)
                .windowed(size, step, partialWindows)
                .map(s -> mapper.apply(s.toList())).iterator(), ORDERED), ORDERED, false);
    }

    public static IntToObjStreamExtension<IntList> intWindowed(int size) {
        return intWindowed(size, 1, false, w -> w);
    }

    public static <R> IntToObjStreamExtension<R> intWindowed(int size,
                                                        int step,
                                                        boolean partialWindows,
                                                        Function<IntList, ? extends R> mapper) {
        return stream -> stream(() -> Spliterators.spliteratorUnknownSize(IntSequence.of(stream::iterator)
                .windowed(size, step, partialWindows)
                .map(mapper).iterator(), ORDERED), ORDERED, false);
    }

    public static <T, R> StreamExtension<T, R> zipWithNext(BiFunction<? super T, ? super T, ? extends R> mapper) {
        return stream -> stream(() -> Spliterators.spliteratorUnknownSize(Sequence.of(stream::iterator)
                .zipWithNext(mapper)
                .iterator(), ORDERED), ORDERED, false);
    }

    public static <T, R> StreamExtension<T, R> scan(R initial, BiFunction<R, T, R> function) {
        return tStream -> stream(() -> Spliterators
                .spliteratorUnknownSize(Sequence.of(tStream::iterator)
                        .scan(initial, function).iterator(), ORDERED), ORDERED, false);
    }

    public static <T> StreamExtension<T, T> filter(Predicate<? super T> predicate) {
        return s -> s.filter(predicate);
    }

    public static <T, R> StreamExtension<T, R> mapNotNull(Function<? super T, R> mapper) {
        return s -> s.filter(Objects::nonNull).map(mapper).filter(Objects::nonNull);
    }
}
