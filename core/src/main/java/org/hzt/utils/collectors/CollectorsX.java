package org.hzt.utils.collectors;

import org.hzt.utils.function.BiFunction;
import org.hzt.utils.function.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CollectorsX {

    private CollectorsX() {
    }

    public static <T> Collector<T, Set<T>, Set<T>> toSet() {
        return new Collector<T, Set<T>, Set<T>>() {

            public Set<T> supply() {
                return new HashSet<T>();
            }

            public void accumulate(final Set<T> acc, final T t) {
                acc.add(t);
            }

            public Set<T> finish(final Set<T> t) {
                return Collections.unmodifiableSet(t);
            }
        };
    }

    public static <K, T> Collector<T, Map<K, List<T>>, Map<K, List<T>>> groupBy(final Function<? super T, ? extends K> selector) {
        return new Collector<T, Map<K, List<T>>, Map<K, List<T>>>() {
            public Map<K, List<T>> supply() {
                return new HashMap<K, List<T>>();
            }

            public void accumulate(final Map<K, List<T>> map, final T t) {
                List<T> l = map.get(selector.apply(t));
                if (l == null) {
                    final List<T> list = new ArrayList<T>();
                    list.add(t);
                    map.put(selector.apply(t), list);
                } else {
                    l.add(t);
                }
            }

            public Map<K, List<T>> finish(final Map<K, List<T>> map) {
                return Collections.unmodifiableMap(map);
            }
        };
    }

    public static <K, T, A, R> Collector<T, Map<K, A>, Map<K, R>> groupBy(
            final Function<? super T, ? extends K> selector,
            final Collector<T, A, R> downsStreamCollector
    ) {
        return new Collector<T, Map<K, A>, Map<K, R>>() {
            public Map<K, A> supply() {
                return new HashMap<K, A>();
            }

            public void accumulate(final Map<K, A> map, final T t) {
                A a = map.get(selector.apply(t));
                if (a == null) {
                    final A aNew = downsStreamCollector.supply();
                    downsStreamCollector.accumulate(aNew, t);
                    map.put(selector.apply(t), aNew);
                } else {
                    downsStreamCollector.accumulate(a, t);
                }
            }

            public Map<K, R> finish(final Map<K, A> map) {
                final Map<K, R> result = new HashMap<K, R>(map.size());
                for (final Map.Entry<K, A> e : map.entrySet()) {
                    result.put(e.getKey(), downsStreamCollector.finish(e.getValue()));
                }
                return result;
            }
        };
    }

    public static <T> Collector<T, long[], Long> count() {
        return new Collector<T, long[], Long>() {

            public long[] supply() {
                return new long[1];
            }

            public void accumulate(final long[] acc, final T t) {
                acc[0]++;
            }

            public Long finish(final long[] acc) {
                return acc[0];
            }
        };
    }

    public static <T> Collector<T, ?, Integer> sumOf(final Function<T, Integer> toIntMapper) {
        return fold(0, new BiFunction<Integer, T, Integer>() {
            public Integer apply(final Integer acc, final T s) {
                return acc + toIntMapper.apply(s);
            }
        });
    }

    public static <T, R> Collector<T, ?, R> fold(final R initial, final BiFunction<R, T, R> accumulator) {
        class Box {
            R result = initial;
        }
        return new Collector<T, Box, R>() {

            public Box supply() {
                return new Box();
            }

            public void accumulate(final Box box, final T t) {
                box.result = accumulator.apply(box.result, t);
            }

            public R finish(final Box box) {
                return box.result;
            }
        };
    }
}
