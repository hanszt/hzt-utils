package org.hzt.utils;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

public final class Timer<R> {

    private final R result;
    private final long durationInNanos;

    private Timer(final R result, final long durationInNanos) {
        this.result = result;
        this.durationInNanos = durationInNanos;
    }

    private Timer(final long durationInNanos) {
        this(null, durationInNanos);
    }

    public R getResult() {
        return result;
    }

    public double getDurationInMillis() {
        return durationInNanos / 1e6;
    }

    public Duration getDuration() {
        return Duration.ofNanos(durationInNanos);
    }

    public String formattedDurationInSeconds() {
        final long seconds = getDuration().getSeconds();
        final long absSeconds = Math.abs(seconds);
        final String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    /**
     * @param t        the parameter the function is applied to
     * @param function the function that is applied to the parameter t
     * @param <T>      The type of the input parameter
     * @param <R>      The type of the output parameter
     * @return first Timer object that contains the time it took to execute the function and the result of the function
     */
    @SuppressWarnings("unused")
    public static <T, R> Timer<R> timeAFunction(final T t, final Function<? super T, ? extends R> function) {
        final long start = System.nanoTime();
        final R result = function.apply(t);
        final long delta = System.nanoTime() - start;
        return new Timer<>(result, delta);
    }

    public static <R> Timer<R> timeALongFunction(final long aLong, final LongFunction<? extends R> function) {
        final long start = System.nanoTime();
        final R result = function.apply(aLong);
        final long delta = System.nanoTime() - start;
        return new Timer<>(result, delta);
    }

    public static <R> Timer<R> timeAnIntFunction(final int integer, final IntFunction<? extends R> function) {
        final long start = System.nanoTime();
        final R result = function.apply(integer);
        final long delta = System.nanoTime() - start;
        return new Timer<>(result, delta);
    }

    /**
     * @param t        the parameter consumed
     * @param consumer the consumer that consumes the parameter t
     * @param <T>      The type of the parameter consumed
     * @return first Timer object that contains the time it took to execute the consumer
     */
    public static <T> Timer<Void> timeAConsumer(final T t, final Consumer<? super T> consumer) {
        final long start = System.nanoTime();
        consumer.accept(t);
        final long delta = System.nanoTime() - start;
        return new Timer<>(delta);
    }

    public static Timer<Void> timeARunnable(final Runnable runnable) {
        final long start = System.nanoTime();
        runnable.run();
        final long delta = System.nanoTime() - start;
        return new Timer<>(delta);
    }
}
