package org.hzt.utils.gatherers;

import module hzt.utils.core;
import module java.base;

import java.util.stream.Gatherer.Integrator;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.function.Predicate.not;

public final class GatherersX {

    private GatherersX() {
    }

    public static <T, R> Gatherer<T, Void, R> map(final Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "'mapper' must not be null");
        return Gatherer.of((_, item, downstream) -> downstream.push(mapper.apply(item)));
    }

    public static <T> Gatherer<T, Void, T> filter(final Predicate<? super T> predicate) {
        return filter(predicate, true);
    }

    public static <T> Gatherer<T, Void, T> filterNot(final Predicate<? super T> predicate) {
        return filter(predicate, false);
    }

    private static <T> Gatherer<T, Void, T> filter(final Predicate<? super T> predicate, final boolean push) {
        Objects.requireNonNull(predicate, "'predicate' must not be null");
        return Gatherer.of((_, item, downstream) -> {
            if (predicate.test(item) == push) {
                return downstream.push(item);
            }
            return !downstream.isRejecting();
        });
    }

    public static <T, R> Gatherer<T, Void, R> mapNotNull(final Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "'mapper' must not be null");
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

    public static <T, R> Gatherer<T, ?, R> mapIndexed(final IndexedFunction<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "'mapper' must not be null");
        return mapMultiIndexed((i, v, c) -> c.accept(mapper.apply(i, v)));
    }

    public static <T> Gatherer<T, ?, T> filterIndexed(final IndexedPredicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "'predicate' must not be null");
        return mapMultiIndexed((i, v, c) -> {
            if (predicate.test(i, v)) {
                c.accept(v);
            }
        });
    }

    public static <T> Gatherer<T, ?, IndexedValue<T>> withIndex() {
        return mapIndexed(IndexedValue::new);
    }

    public static <T, R> Gatherer<T, ?, R> flatMapIndexed(final IndexedFunction<? super T, ? extends Iterable<R>> toIterableMapper) {
        Objects.requireNonNull(toIterableMapper, "'toIterableMapper' must not be null");
        return mapMultiIndexed((i, v, c) -> toIterableMapper.apply(i, v).forEach(c));
    }

    public static <T, R> Gatherer<T, ?, R> mapMultiIndexed(final IndexedBiConsumer<T, Consumer<R>> consumer) {
        Objects.requireNonNull(consumer, "'consumer' must not be null");
        return Gatherer.ofSequential(
                () -> new Object() {
                    int index = 0;
                    boolean proceed = true;
                },
                Integrator.ofGreedy((s, item, downstream) -> {
                    consumer.accept(s.index++, item, element -> s.proceed = downstream.push(element));
                    return s.proceed && !downstream.isRejecting();
                }));
    }

    @FunctionalInterface
    public interface IndexedBiConsumer<T, U> {
        void accept(int index, T t, U u);
    }

    public static <T, R> Gatherer<T, Void, R> flatMap(final Function<? super T, ? extends Iterable<R>> toIterableMapper) {
        Objects.requireNonNull(toIterableMapper, "'toIterableMapper' must not be null");
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

    public static <T, R> Gatherer<T, Void, R> mapMulti(final BiConsumer<? super T, Consumer<? super R>> consumer) {
        Objects.requireNonNull(consumer, "'consumer' must not be null");
        return Gatherer.of(Integrator.ofGreedy((_, item, downstream) -> {
            consumer.accept(item, downstream::push);
            return !downstream.isRejecting();
        }));
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
        return Gatherer.ofSequential(Counter::new, (s, item, downstream) -> {
            if (s.count++ < max) {
                return !downstream.isRejecting() && downstream.push(item);
            }
            return false;
        });
    }

    public static <T> Gatherer<T, ?, T> dropWhile(final Predicate<? super T> condition) {
        Objects.requireNonNull(condition, "'condition' must not be null");
        return Gatherer.ofSequential(() -> new Object() {
            boolean firstSeen = false;
        }, (s, item, downstream) -> {
            if (s.firstSeen) {
                return !downstream.isRejecting() && downstream.push(item);
            }
            if (!condition.test(item)) {
                s.firstSeen = true;
                downstream.push(item);
            }
            return !downstream.isRejecting();
        });
    }

    public static <T> Gatherer<T, ?, T> takeWhile(final Predicate<? super T> condition) {
        Objects.requireNonNull(condition, "'condition' must not be null");
        return Gatherer.ofSequential((_, item, downStream) -> {
            final var test = !downStream.isRejecting() && condition.test(item);
            if (test) {
                downStream.push(item);
            }
            return test;
        });
    }

    public static <T> Gatherer<T, ?, T> takeWhileIncluding(final Predicate<? super T> condition) {
        Objects.requireNonNull(condition, "'condition' must not be null");
        return Gatherer.ofSequential((_, item, downStream) -> {
            downStream.push(item);
            return !downStream.isRejecting() && condition.test(item);
        });
    }

    public static <T, R> Gatherer<T, ?, T> distinctBy(final Function<? super T, ? extends R> selector) {
        Objects.requireNonNull(selector);
        return Gatherer.ofSequential(HashSet<R>::new,
                Integrator.ofGreedy((set, item, downstream) -> {
            if (set.add(selector.apply(item))) {
                downstream.push(item);
            }
            return !downstream.isRejecting();
                }));
    }

    public static <T> Gatherer<T, ?, T> sorted(final Comparator<T> comparator) {
        Objects.requireNonNull(comparator);
        return Gatherer.ofSequential(ArrayList<T>::new,
                (list, item, _) -> list.add(item),
                (list, downstream) -> {
                    list.sort(comparator);
                    list.forEach(downstream::push);
                });
    }

    public static <T> Gatherer<T, ?, T> sortedDistinct(final Comparator<T> comparator) {
        Objects.requireNonNull(comparator);
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
        Objects.requireNonNull(selector);
        return Gatherer.ofSequential(IntStatistics::new, (stats, item, downstream) -> {
            stats.accept(selector.applyAsInt(item));
            return downstream.push(new IntStatistics().combine(stats));
        });
    }

    public static <T> Gatherer<T, ?, LongStatistics> runningLongStatisticsOf(final ToLongFunction<? super T> selector) {
        Objects.requireNonNull(selector);
        return Gatherer.ofSequential(LongStatistics::new, (stats, item, downstream) -> {
            stats.accept(selector.applyAsLong(item));
            return downstream.push(new LongStatistics().combine(stats));
        });
    }

    public static <T> Gatherer<T, ?, DoubleStatistics> runningDoubleStatisticsOf(final ToDoubleFunction<? super T> selector) {
        Objects.requireNonNull(selector);
        return Gatherer.ofSequential(DoubleStatistics::new, (stats, item, downstream) -> {
            stats.accept(selector.applyAsDouble(item));
            return downstream.push(new DoubleStatistics().combine(stats));
        });
    }

    public static <T, R> Gatherer<T, ?, R> zip(Stream<T> other, BiFunction<? super T, ? super T, ? extends R> zipper) {
        return zip(other::iterator, zipper);
    }

    public static <T, R> Gatherer<T, ?, R> zip(Iterable<T> other, BiFunction<? super T, ? super T, ? extends R> zipper) {
        Objects.requireNonNull(other);
        Objects.requireNonNull(zipper);
        return Gatherer.ofSequential(other::iterator,
                (iterator, e, d) -> iterator.hasNext() && d.push(zipper.apply(e, iterator.next()))
        );
    }

    public static <T, R> Gatherer<T, ?, R> zipWithNext(final BiFunction<? super T, ? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "'mapper' must not be null");
        return Gatherer.ofSequential(
                () -> new Object() {
                    boolean hasPrev = false;
                    T prev = null;
                },
                (s, v, ds) -> {
                    final var proceed = !s.hasPrev || ds.push(mapper.apply(s.prev, v));
                    s.prev = v;
                    s.hasPrev = true;
                    return proceed && !ds.isRejecting();
                }
        );
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
