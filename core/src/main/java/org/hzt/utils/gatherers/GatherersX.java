package org.hzt.utils.gatherers;

import org.hzt.utils.statistics.DoubleStatistics;
import org.hzt.utils.statistics.IntStatistics;
import org.hzt.utils.statistics.LongStatistics;
import org.hzt.utils.tuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Gatherer;
import java.util.stream.Gatherer.Integrator;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.function.Predicate.not;

public final class GatherersX {

    private GatherersX() {
    }

    public static <T, R> Gatherer<T, Void, R> map(final Function<? super T, ? extends R> mapper) {
        return Gatherer.of((_, item, downstream) -> downstream.push(mapper.apply(item)));
    }

    public static <T> Gatherer<T, Void, T> filter(final Predicate<? super T> predicate) {
        return filter(predicate, true);
    }

    public static <T> Gatherer<T, Void, T> filterNot(final Predicate<? super T> predicate) {
        return filter(predicate, false);
    }

    private static <T> Gatherer<T, Void, T> filter(final Predicate<? super T> predicate, final boolean push) {
        return Gatherer.of((_, item, downstream) -> {
            if (predicate.test(item) == push) {
                downstream.push(item);
            }
            return !downstream.isRejecting();
        });
    }

    public static <T, R> Gatherer<T, Void, R> mapNotNull(final Function<? super T, ? extends R> mapper) {
        return Gatherer.of((_, t, downstream) -> acceptIfResultNotNull(mapper, t, downstream::push));
    }

    static <T, R> boolean acceptIfResultNotNull(final Function<? super T, ? extends R> mapper, final T t, final Consumer<R> consumer) {
        if (t != null) {
            final var element = mapper.apply(t);
            if (element != null) {
                consumer.accept(element);
            }
        }
        return true;
    }

    public static <T, R> Gatherer<T, ?, R> mapIndexed(final BiFunction<Integer, ? super T, ? extends R> mapper) {
        return Gatherer.ofSequential(
                () -> new Object() {
                    int index = 0;
                },
                (state, item, downstream) -> downstream.push(mapper.apply(state.index++, item))
        );
    }

    public static <T, R> Gatherer<T, Void, R> flatMap(final Function<? super T, ? extends Iterable<R>> toIterableMapper) {
        return Gatherer.of((_, item, downstream) -> {
            toIterableMapper.apply(item).forEach(downstream::push);
            return true;
        });
    }

    public static <T, I extends Iterable<? extends T>> Gatherer<I, Void, T> flatten() {
        return Gatherer.of(Integrator.ofGreedy((_, iterable, downstream) -> {
            iterable.forEach(downstream::push);
            return true;
        }));
    }

    public static <T, R> Gatherer<T, Void, R> mapMulti(final BiConsumer<? super T, Consumer<? super R>> mapper) {
        return Gatherer.of((_, item, downstream) -> {
            mapper.accept(item, downstream::push);
            return !downstream.isRejecting();
        });
    }

    public static <T> Gatherer<T, ?, T> skip(final long n) {
        return Gatherer.ofSequential(Counter::new, (count, item, downStream) -> {
            if (count.count++ >= n) {
                return !downStream.isRejecting() && downStream.push(item);
            }
            return true;
        });
    }

    public static <T> Gatherer<T, ?, T> limit(final long max) {
        return Gatherer.ofSequential(Counter::new, (count, item, downStream) -> {
            if (count.count++ < max) {
                return !downStream.isRejecting() && downStream.push(item);
            }
            return false;
        });
    }

    public static <T> Gatherer<T, ?, T> dropWhile(final Predicate<? super T> condition) {
        return Gatherer.ofSequential(() -> new Object() {
            boolean value = false;
        }, (firstSeen, item, downStream) -> {
            if (firstSeen.value) {
                return !downStream.isRejecting() && downStream.push(item);
            }
            if (!condition.test(item)) {
                firstSeen.value = true;
                downStream.push(item);
            }
            return !downStream.isRejecting();
        });
    }

    public static <T> Gatherer<T, ?, T> takeWhile(final Predicate<? super T> condition) {
        return Gatherer.ofSequential((_, item, downStream) -> {
            final var test = !downStream.isRejecting() && condition.test(item);
            if (test) {
                downStream.push(item);
            }
            return test;
        });
    }

    public static <T> Gatherer<T, ?, T> takeWhileIncluding(final Predicate<? super T> condition) {
        return Gatherer.ofSequential((_, item, downStream) -> {
            downStream.push(item);
            return !downStream.isRejecting() && condition.test(item);
        });
    }

    public static <T, R> Gatherer<T, ?, T> distinctBy(final Function<? super T, ? extends R> selector) {
        return Gatherer.ofSequential(HashSet<R>::new, (set, item, downstream) -> {
            if (set.add(selector.apply(item))) {
                downstream.push(item);
            }
            return !downstream.isRejecting();
        });
    }

    public static <T> Gatherer<T, ?, T> sorted(final Comparator<T> comparator) {
        return Gatherer.ofSequential(ArrayList<T>::new,
                (list, item, _) -> list.add(item),
                (list, downstream) -> {
                    list.sort(comparator);
                    list.forEach(downstream::push);
                });
    }

    public static <T> Gatherer<T, ?, T> sortedDistinct(final Comparator<T> comparator) {
        return Gatherer.ofSequential(() -> new TreeSet<>(comparator),
                (set, item, downstream) -> {
                    set.add(item);
                    return !downstream.isRejecting();
                },
                (set, downstream) -> set
                        .stream()
                        .takeWhile(not(_ -> downstream.isRejecting()))
                        .forEach(downstream::push));
    }

    public static <T extends Comparable<? super T>> Gatherer<T, ?, T> sortedDistinct() {
        return sortedDistinct(Comparator.<T>naturalOrder());
    }

    public static <T extends Comparable<? super T>> Gatherer<T, ?, T> sorted() {
        return sorted(Comparator.<T>naturalOrder());
    }

    public static <T, R extends Comparable<? super R>> Gatherer<T, ?, T> sortedBy(final Function<? super T, ? extends R> selector) {
        return sorted(comparing(selector));
    }

    public static <T, R extends Comparable<? super R>> Gatherer<T, ?, T> sortedDescendingBy(final Function<? super T, ? extends R> selector) {
        return sorted(comparing(selector, reverseOrder()));
    }

    public static <T> Gatherer<T, ?, IntStatistics> runningIntStatisticsOf(final ToIntFunction<? super T> selector) {
        return Gatherer.ofSequential(IntStatistics::new, (stats, item, downstream) -> {
            stats.accept(selector.applyAsInt(item));
            return downstream.push(new IntStatistics().combine(stats));
        });
    }

    public static <T> Gatherer<T, ?, LongStatistics> runningLongStatisticsOf(final ToLongFunction<? super T> selector) {
        return Gatherer.ofSequential(LongStatistics::new, (stats, item, downstream) -> {
            stats.accept(selector.applyAsLong(item));
            return downstream.push(new LongStatistics().combine(stats));
        });
    }

    public static <T> Gatherer<T, ?, DoubleStatistics> runningDoubleStatisticsOf(final ToDoubleFunction<? super T> selector) {
        return Gatherer.ofSequential(DoubleStatistics::new, (stats, item, downstream) -> {
            stats.accept(selector.applyAsDouble(item));
            return downstream.push(new DoubleStatistics().combine(stats));
        });
    }

    public static <T, R> Gatherer<T, ?, R> zip(Stream<T> other, BiFunction<? super T, ? super T, ? extends R> zipper) {
        return zip(other::iterator, zipper);
    }

    public static <T, R> Gatherer<T, ?, R> zip(Iterable<T> other, BiFunction<? super T, ? super T, ? extends R> zipper) {
        return Gatherer.ofSequential(other::iterator,
                (iterator, e, d) -> iterator.hasNext() && d.push(zipper.apply(e, iterator.next()))
        );
    }

    public static <T, R> Gatherer<T, ?, R> zipWithNext(final BiFunction<? super T, ? super T, ? extends R> mapper) {
        return Gatherers.<T>windowSliding(2)
                .andThen(Gatherer.ofSequential((_, w, downstream) -> downstream.push(mapper.apply(w.getFirst(), w.get(1)))));
    }

    public static <T> Gatherer<T, ?, Pair<T, T>> zipWithNext() {
        return Gatherers.<T>windowSliding(2).andThen(map(l -> Pair.of(l.getFirst(), l.getLast())));
    }

    public static <T> Gatherer<T, ?, List<T>> chunked(final int size) {
        return windowed(size, size, true);
    }

    public static <T> Gatherer<T, ?, List<T>> windowed(final int size) {
        return windowed(size, 1, false);
    }

    public static <T> Gatherer<T, ?, List<T>> windowed(final int size, final int step) {
        return windowed(size, step, false);
    }

    public static <T> Gatherer<T, ?, List<T>> windowed(
            final int size,
            final int step,
            final boolean partialWindows
    ) {
        if (size < 1) {
            throw new IllegalArgumentException("'size' must be greater than zero");
        }
        if (step < 1) {
            throw new IllegalArgumentException("'step' must be greater than zero");
        }
        class Window {
            Object[] nextWindow = new Object[size];
            int cursor = 0;
            boolean firstWindow = true;

            @SuppressWarnings("unchecked")
            boolean integrate(T element, Gatherer.Downstream<? super List<T>> downstream) {
                final var nextIndex = cursor++;
                if (nextIndex < nextWindow.length) {
                    nextWindow[nextIndex] = Objects.requireNonNull(element, "Element in window must not be null");
                }
                if (cursor < size) {
                    return true;
                }
                final var nextWindow = this.nextWindow;
                if (step < size) {
                    final var newWindow = new Object[size];
                    System.arraycopy(nextWindow, step, newWindow, 0, size - step);
                    this.nextWindow = newWindow;
                    cursor -= step;
                } else {
                    if (cursor < step) {
                        return true;
                    }
                    cursor = 0;
                }
                firstWindow = false;
                return downstream.push(List.of((T[]) Arrays.copyOf(nextWindow, nextWindow.length)));
            }

            @SuppressWarnings("unchecked")
            void finish(Gatherer.Downstream<? super List<T>> downstream) {
                while ((partialWindows || firstWindow) && cursor != 0 && nextWindow.length != 0 && !downstream.isRejecting()) {
                    final var nextWindow = this.nextWindow;
                    final var thisCursor = cursor;
                    if (step < size) {
                        if (partialWindows) {
                            downstream.push(List.of((T[]) Arrays.copyOf(nextWindow, thisCursor)));
                        }
                        firstWindow = false;
                        final var newWindowLength = cursor - step;
                        if (newWindowLength < 0) {
                            break;
                        }
                        final var newWindow = new Object[cursor];
                        System.arraycopy(nextWindow, step, newWindow, 0, newWindowLength);
                        this.nextWindow = newWindow;
                        cursor -= step;
                    } else {
                        if (thisCursor >= 0) {
                            downstream.push(List.of((T[]) Arrays.copyOf(nextWindow, Math.min(this.nextWindow.length, thisCursor))));
                            firstWindow = false;
                            cursor -= step;
                        } else {
                            cursor = firstWindow ? this.nextWindow.length : 0;
                        }
                    }
                }
            }
        }
        return Gatherer.<T, Window, List<T>>ofSequential(
                Window::new,
                Integrator.<Window, T, List<T>>ofGreedy(Window::integrate),
                Window::finish
        );
    }

    public static <T> Gatherer<T, ?, List<T>> nextWindowIf(Predicate<? super T> predicate) {
        class Window {
            final List<T> windowBuilder = new ArrayList<>();

            boolean integrate(T element, Gatherer.Downstream<? super List<T>> downstream) {
                windowBuilder.add(element);
                if (predicate.test(element)) {
                    final var next = List.copyOf(windowBuilder);
                    windowBuilder.clear();
                    return downstream.push(next);
                }
                return !downstream.isRejecting();
            }

            void finish(Gatherer.Downstream<? super List<T>> downstream) {
                if (!windowBuilder.isEmpty() && !downstream.isRejecting()) {
                    downstream.push(List.copyOf(windowBuilder));
                }
            }
        }
        return Gatherer.<T, Window, List<T>>ofSequential(
                Window::new,
                Integrator.<Window, T, List<T>>ofGreedy(Window::integrate),
                Window::finish
        );
    }

    public static <T> Gatherer<T, ?, List<T>> filterZippedWithNext(final BiPredicate<? super T, ? super T> predicate) {
        return Gatherers.<T>windowSliding(2)
                .andThen(filter(s -> predicate.test(s.getFirst(), s.get(1))));
    }

    public static <T> Gatherer<T, ?, T> single() {
        return Gatherer.ofSequential(
                () -> new Object() {
                    boolean hasSingle = false;
                    T single = null;
                },
                (s, item, downstream) -> {
                    if (s.hasSingle) {
                        s.hasSingle = false;
                        return false;
                    }
                    s.single = item;
                    s.hasSingle = true;
                    return !downstream.isRejecting();
                },
                (s, downstream) -> {
                    if (s.hasSingle && !downstream.isRejecting()) {
                        downstream.push(s.single);
                    }
                }
        );
    }

    private static final class Counter {
        long count = 0;
    }
}
