package org.hzt.utils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class TimingUtils {

    private static final String DO_NOT_USE_PRINT_IN_PRODUCTION_CODE = "java:S106";

    private TimingUtils() {
    }

    public static void sleep(Duration duration) {
        try {
            TimeUnit.MILLISECONDS.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @SuppressWarnings(DO_NOT_USE_PRINT_IN_PRODUCTION_CODE)
    public static void main(String... args) {
        System.out.println("Start sleeping...");
        final double startTime = System.nanoTime();
        sleep(Duration.ofMillis(1_000));
        final double delta = System.nanoTime() - startTime;
        System.out.printf("Slept for %.5f millis", delta * 1e-6);
    }
}
