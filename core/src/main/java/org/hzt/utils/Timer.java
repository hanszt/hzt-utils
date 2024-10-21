package org.hzt.utils;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

public final class Timer<R> {

    private final R result;
    private final Duration duration;

    private Timer(final R result, final Duration duration) {
        this.result = result;
        this.duration = duration;
    }

    public R getResult() {
        return result;
    }

    public Duration getDuration() {
        return duration;
    }

    public String formattedDurationInSeconds() {
        return String.format("%2d:%02d s", duration.toSecondsPart(), duration.toMillisPart());
    }

    public static <T> Timer<T> measureTimedValue(final Supplier<T> supplier) {
        final var start = System.currentTimeMillis();
        final var result = supplier.get();
        final var elapsed = System.currentTimeMillis() - start;
        return new Timer<>(result, Duration.ofMillis(elapsed));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof final Timer<?> timer)) return false;

        return Objects.equals(result, timer.result) && Objects.equals(duration, timer.duration);
    }

    @Override
    public int hashCode() {
        int result1 = Objects.hashCode(result);
        result1 = 31 * result1 + Objects.hashCode(duration);
        return result1;
    }

    @Override
    public String toString() {
        return "Timer{" +
               "result=" + result +
               ", duration=" + duration +
               '}';
    }
}
