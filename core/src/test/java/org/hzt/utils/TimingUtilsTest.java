package org.hzt.utils;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.hzt.utils.TimingUtils.sleep;
import static org.junit.jupiter.api.Assertions.*;

class TimingUtilsTest {

    @Test
    void testSleepFiftyMillis() {
        final var duration = 50;
        final var time = Timer.timeARunnable(() -> sleepAndPrint(duration));

        final var millis = time.getDuration().toMillis();
        assertAll(
                () -> assertTrue(millis >= duration),
                () -> assertTrue(millis < duration * 2)
        );
    }

    @SuppressWarnings("SameParameterValue")
    private static void sleepAndPrint(int millis) {
        System.out.println("Start sleeping...");
        final double startTime = System.nanoTime();
        sleep(Duration.ofMillis(millis));
        final double delta = System.nanoTime() - startTime;
        System.out.printf("Slept for %.5f millis", delta * 1e-6);
    }
}
