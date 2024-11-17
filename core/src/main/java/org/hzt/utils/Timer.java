package org.hzt.utils;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

public final class Timer<R> {
    private final R result;
    private final Duration duration;

    private Timer(R result, Duration duration) {
        this.result = result;
        this.duration = duration;
    }

    public String formattedDurationInSeconds() {
        return String.format("%2d:%02d s", duration.toSecondsPart(), duration.toMillisPart());
    }

    public static <T> Timer<T> measureTimedValue(final Supplier<T> supplier) {
        final var start = System.currentTimeMillis();
        final var result = supplier.get();
        return new Timer<>(result, Duration.ofMillis(System.currentTimeMillis() - start));
    }

    public R result() {
        return result;
    }

    public Duration duration() {
        return duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Timer<?> that)) return false;
        return Objects.equals(this.result, that.result) &&
                Objects.equals(this.duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, duration);
    }

    @Override
    public String toString() {
        return "Timer[" +
                "result=" + result + ", " +
                "duration=" + duration + ']';
    }

}
