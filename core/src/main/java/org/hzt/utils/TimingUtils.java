package org.hzt.utils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class TimingUtils {

    private TimingUtils() {
    }

    public static void sleep(final Duration duration) {
        try {
            TimeUnit.MILLISECONDS.sleep(duration.toMillis());
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
