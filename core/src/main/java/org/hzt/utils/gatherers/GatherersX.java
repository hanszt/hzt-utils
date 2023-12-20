package org.hzt.utils.gatherers;

import org.hzt.utils.statistics.DoubleStatistics;
import org.hzt.utils.statistics.IntStatistics;
import org.hzt.utils.statistics.LongStatistics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

public final class GatherersX {

    private GatherersX() {
    }

    public static <T, R> Gatherer<T, Void, R> map(Function<? super T, ? extends R> mapper) {
        return Gatherer.of((unused, item, downstream) -> downstream.push(mapper.apply(item)));
    }

    public static <T> Gatherer<T, Void, T> filter(Predicate<? super T> predicate) {
        return filter(predicate, true);
    }

    public static <T> Gatherer<T, Void, T> filterNot(Predicate<? super T> predicate) {
        return filter(predicate, false);
    }

    private static <T> Gatherer<T, Void, T> filter(Predicate<? super T> predicate, boolean push) {
        return Gatherer.of((unused, item, downstream) -> {
            if (predicate.test(item) == push) {
                downstream.push(item);
            }
            return true;
        });
    }

    public static <T, R> Gatherer<T, Void, R> mapNotNull(Function<? super T, ? extends R> mapper) {
        return Gatherer.of((unused, t, downstream) -> acceptIfResultNotNull(mapper, t, downstream::push));
    }

    static <T, R> boolean acceptIfResultNotNull(Function<? super T, ? extends R> mapper, T t, Consumer<R> consumer) {
        if (t != null) {
            final var element = mapper.apply(t);
            if (element != null) {
                consumer.accept(element);
            }
        }
        return true;
    }

    public static <T, R> Gatherer<T, ?, R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        return Gatherer.ofSequential(
                AtomicInteger::new,
                (index, item, downstream) -> downstream.push(mapper.apply(index.getAndIncrement(), item))
        );
    }

    public static <T, R> Gatherer<T, Void, R> flatMap(final Function<? super T, ? extends Iterable<R>> toIterableMapper) {
        return Gatherer.of((unused, item, downstream) -> {
            toIterableMapper.apply(item).forEach(downstream::push);
            return true;
        });
    }

    public static <T, I extends Iterable<? extends T>> Gatherer<I, Void, T> flatten() {
        return Gatherer.of((unused, iterable, downstream) -> {
            iterable.forEach(downstream::push);
            return true;
        });
    }

    public static <T, R> Gatherer<T, Void, R> mapMulti(BiConsumer<? super T, Consumer<? super R>> mapper) {
        return Gatherer.of((unused, item, downstream) -> {
            mapper.accept(item, downstream::push);
            return true;
        });
    }

    public static <T> Gatherer<T, ?, T> skip(long n) {
        return Gatherer.ofSequential(AtomicLong::new, (count, item, downStream) -> {
            if (count.getAndIncrement() >= n) {
                return downStream.push(item);
            }
            return true;
        });
    }

    public static <T> Gatherer<T, ?, T> limit(long max) {
        return Gatherer.ofSequential(AtomicLong::new, (count, item, downStream) -> {
            if (count.getAndIncrement() < max) {
                return downStream.push(item);
            }
            return false;
        });
    }

    public static <T> Gatherer<T, ?, T> dropWhile(Predicate<T> condition) {
        return Gatherer.ofSequential(AtomicBoolean::new, (firstSeen, item, downStream) -> {
            if (firstSeen.get()) {
                downStream.push(item);
                return true;
            }
            if (!condition.test(item)) {
                firstSeen.set(true);
                downStream.push(item);
            }
            return true;
        });
    }

    public static <T> Gatherer<T, ?, T> takeWhile(Predicate<T> condition) {
        return Gatherer.ofSequential((unused, item, downStream) -> {
            final var test = condition.test(item);
            if (test) {
                downStream.push(item);
            }
            return test;
        });
    }

    public static <T> Gatherer<T, ?, T> takeWhileIncluding(Predicate<T> condition) {
        return Gatherer.ofSequential((unused, item, downStream) -> {
            downStream.push(item);
            return condition.test(item);
        });
    }

    public static <T, R> Gatherer<T, ?, T> distinctBy(Function<? super T, ? extends R> selector) {
        return Gatherer.ofSequential(HashSet<R>::new, (set, item, downstream) -> {
            if (set.add(selector.apply(item))) {
                downstream.push(item);
            }
            return true;
        });
    }

    public static <T> Gatherer<T, ?, T> sorted(Comparator<T> comparator) {
        return Gatherer.ofSequential(ArrayList<T>::new,
                (list, item, unused) -> list.add(item),
                (list, downstream) -> {
                    list.sort(comparator);
                    list.forEach(downstream::push);
                });
    }

    public static <T> Gatherer<T, ?, T> sortedDistinct(Comparator<T> comparator) {
        return Gatherer.ofSequential(() -> new TreeSet<>(comparator),
                (set, item, unused) -> {
                    set.add(item);
                    return true;
                },
                (set, downstream) -> set.forEach(downstream::push));
    }

    public static <T extends Comparable<? super T>> Gatherer<T, ?, T> sortedDistinct() {
        return sortedDistinct(Comparator.<T>naturalOrder());
    }

    public static <T extends Comparable<? super T>> Gatherer<T, ?, T> sorted() {
        return sorted(Comparator.<T>naturalOrder());
    }

    public static <T, R extends Comparable<? super R>> Gatherer<T, ?, T> sortedBy(Function<? super T, ? extends R> selector) {
        return sorted(comparing(selector));
    }

    public static <T, R extends Comparable<? super R>> Gatherer<T, ?, T> sortedDescendingBy(Function<? super T, ? extends R> selector) {
        return sorted(comparing(selector, reverseOrder()));
    }

    public static <T> Gatherer<T, ?, IntStatistics> runningIntStatisticsOf(ToIntFunction<? super T> selector) {
        return Gatherer.ofSequential(IntStatistics::new, (stats, item, downstream) -> {
            stats.accept(selector.applyAsInt(item));
            return downstream.push(new IntStatistics().combine(stats));
        });
    }

    public static <T> Gatherer<T, ?, LongStatistics> runningLongStatisticsOf(ToLongFunction<? super T> selector) {
        return Gatherer.ofSequential(LongStatistics::new, (stats, item, downstream) -> {
            stats.accept(selector.applyAsLong(item));
            return downstream.push(new LongStatistics().combine(stats));
        });
    }

    public static <T> Gatherer<T, ?, DoubleStatistics> runningDoubleStatisticsOf(ToDoubleFunction<? super T> selector) {
        return Gatherer.ofSequential(DoubleStatistics::new, (stats, item, downstream) -> {
            stats.accept(selector.applyAsDouble(item));
            return downstream.push(new DoubleStatistics().combine(stats));
        });
    }

    public static <T, R> Gatherer<T, ?, R> zipWithNext(BiFunction<? super T, ? super T, ? extends R> mapper) {
        return Gatherers.<T>windowSliding(2)
                .andThen(Gatherer.ofSequential((unused, w, downstream) -> downstream.push(mapper.apply(w.get(0), w.get(1)))));
    }

    public static <T> Gatherer<T, ?, List<T>> zipWithNext() {
        return Gatherers.windowSliding(2);
    }

    public static <T> Gatherer<T, ?, List<T>> filterZippedWithNext(BiPredicate<? super T, ? super T> predicate) {
        return Gatherers.<T>windowSliding(2)
                .andThen(filter(s -> predicate.test(s.get(0), s.get(1))));
    }
}
