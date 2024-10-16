package org.hzt.utils.gatherers.primitives;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.gatherers.Gatherer.Downstream;
import org.hzt.utils.statistics.IntStatistics;

import java.util.function.IntFunction;

/**
 * A concept for int specialized gatherers
 */
public final class IntGatherers {

    private IntGatherers() {
    }

    public static <R> IntGatherer<Void, R> mapToObjNotNull(final IntFunction<? extends R> toObjMapper) {
        return IntGatherer.of((unused, value, downstream) -> {
            final R element = toObjMapper.apply(value);
            if (element != null) {
                downstream.push(element);
            }
            return true;
        });
    }

    public static IntGatherer<?, IntStatistics> runningStatistics() {
        return IntGatherer.ofSequential(IntStatistics::new, (stats, value, downstream) -> {
            stats.accept(value);
            return downstream.push(new IntStatistics().combine(stats));
        });
    }

    public static IntGatherer<?, IntList> windowSliding(final int windowSize) {
        if (windowSize < 1) {
            throw new IllegalArgumentException("'windowSize' must be greater than zero");
        }
        class SlidingWindow {
            int[] window = new int[windowSize];
            int at = 0;
            boolean firstWindow = true;

            boolean nextWindow(final int element, final Downstream<? super IntList> downstream) {
                window[at++] = element;
                if (at < windowSize) {
                    return true;
                } else {
                    final int[] oldWindow = window;
                    final int[] newWindow = new int[windowSize];
                    System.arraycopy(oldWindow, 1, newWindow, 0, windowSize - 1);
                    window = newWindow;
                    at -= 1;
                    firstWindow = false;
                    return downstream.push(IntList.of(oldWindow));
                }
            }

            void finish(final Downstream<? super IntList> downstream) {
                if (firstWindow && at > 0 && !downstream.isRejecting()) {
                    final int[] lastWindow = window;
                    System.arraycopy(window, 0, lastWindow, 0, at);
                    window = null;
                    at = 0;
                    downstream.push(IntList.of(windowSize));
                }
            }
        }
        return IntGatherer.<SlidingWindow, IntList>ofSequential(
                // Initializer
                SlidingWindow::new,
                // Integrator
                SlidingWindow::nextWindow,
                // Finisher
                SlidingWindow::finish
        );
    }
}
