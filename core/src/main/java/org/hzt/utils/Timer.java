package org.hzt.utils;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

public final class Timer<R> {

    private final R result;
    private final long durationInNanos;

    private Timer(R result, long durationInNanos) {
        this.result = result;
        this.durationInNanos = durationInNanos;
    }

    private Timer(long durationInNanos) {
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
        final var duration = getDuration();
        return String.format("%2d:%02d s", duration.toSecondsPart(), duration.toMillisPart());
    }

    /**
     * @param t the parameter the function is applied to
     * @param function the function that is applied to the parameter t
     * @param <T> The type of the input parameter
     * @param <R> The type of the output parameter
     * @return first Timer object that contains the time it took to execute the function and the result of the function
     */
    @SuppressWarnings("unused")
    public static <T, R> Timer<R> timeAFunction(T t, Function<? super T, ? extends R> function) {
        long start = System.nanoTime();
        R result = function.apply(t);
        long delta = System.nanoTime() - start;
        return new Timer<>(result, delta);
    }

    public static <R> Timer<R> timeALongFunction(long aLong, LongFunction<? extends R> function) {
        long start = System.nanoTime();
        R result = function.apply(aLong);
        long delta = System.nanoTime() - start;
        return new Timer<>(result, delta);
    }

    public static <R> Timer<R> timeAnIntFunction(int integer, IntFunction<? extends R> function) {
        long start = System.nanoTime();
        R result = function.apply(integer);
        long delta = System.nanoTime() - start;
        return new Timer<>(result, delta);
    }

    /**
     * @param t the parameter consumed
     * @param consumer the consumer that consumes the parameter t
     * @param <T> The type of the parameter consumed
     * @return first Timer object that contains the time it took to execute the consumer
     */
    public static <T> Timer<Void> timeAConsumer(T t, Consumer<? super T> consumer) {
        long start = System.nanoTime();
        consumer.accept(t);
        long delta = System.nanoTime() - start;
        return new Timer<>(delta);
    }

    public static Timer<Void> timeARunnable(Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        long delta = System.nanoTime() - start;
        return new Timer<>(delta);
    }
}
