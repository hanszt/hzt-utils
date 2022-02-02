package hzt.collectors;

import hzt.PreConditions;
import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.collections.SetX;
import hzt.function.QuadFunction;
import hzt.function.QuintFunction;
import hzt.function.TriFunction;
import hzt.statistics.DoubleStatistics;
import hzt.stream.StreamUtils;
import hzt.tuples.Pair;
import hzt.tuples.QuadTuple;
import hzt.tuples.QuintTuple;
import hzt.tuples.Triple;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"DuplicatedCode", "unused"})
public final class CollectorsX {

    private CollectorsX() {
    }

    public static <T, A, R>
    Collector<T, ?, R> filtering(Predicate<? super T> predicate,
                                 Collector<? super T, A, R> downstream) {
        BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
        return new CollectorImpl<>(downstream.supplier(),
                (r, t) -> {
                    if (predicate.test(t)) {
                        downstreamAccumulator.accept(r, t);
                    }
                },
                downstream.combiner(), downstream.finisher(),
                downstream.characteristics());
    }

    public static <T, U, A, R>
    Collector<T, ?, R> flatMapping(Function<? super T, ? extends Stream<? extends U>> mapper,
                                   Collector<? super U, A, R> downstream) {
        BiConsumer<A, ? super U> downstreamAccumulator = downstream.accumulator();
        return new CollectorImpl<>(downstream.supplier(),
                (r, t) -> {
                    try (Stream<? extends U> result = mapper.apply(t)) {
                        if (result != null) {
                            result.sequential().forEach(u -> downstreamAccumulator.accept(r, u));
                        }
                    }
                },
                downstream.combiner(), downstream.finisher(),
                downstream.characteristics());
    }

    //Experimental. Runs faster into out of memory error than mapMulti method from Stream. No buffer implemented here
    public static <T, U, A, R> Collector<T, ?, R> multiMapping(BiConsumer<? super T, ? super Consumer<U>> mapper,
                                                               Collector<? super U, A, R> downstream) {
        final BiConsumer<A, T> accumulator = (A a, T t) -> {
            final Consumer<U> uConsumer = (U u) -> downstream.accumulator().accept(a, u);
            mapper.accept(t, uConsumer);
        };
        return new CollectorImpl<>(downstream.supplier(),
                accumulator,
                downstream.combiner(),
                downstream.finisher(),
                downstream.characteristics());
    }

    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> toMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> toUnModifiableMap() {
        return toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <T> Collector<T, ?, List<T>> filteringToList(Predicate<? super T> predicate) {
        return filtering(predicate, toUnmodifiableList());
    }

    public static <T, R> Collector<T, ?, List<R>> mappingToList(Function<? super T, ? extends R> mapper) {
        return Collectors.mapping(mapper, toUnmodifiableList());
    }

    public static <T, R> Collector<T, ?, List<R>> multiMappingToList(BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return multiMapping(mapper, toUnmodifiableList());
    }

    public static <T, R> Collector<T, ?, List<R>> flatMappingToList(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return flatMapping(mapper, toUnmodifiableList());
    }

    public static <T> Collector<T, ?, Set<T>> filteringToSet(Predicate<? super T> predicate) {
        return filtering(predicate, toUnmodifiableSet());
    }

    public static <T, R> Collector<T, ?, Set<R>> mappingToSet(Function<? super T, ? extends R> mapper) {
        return Collectors.mapping(mapper, toUnmodifiableSet());
    }

    public static <T, R> Collector<T, ?, Set<R>> multiMappingToSet(BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return multiMapping(mapper, toUnmodifiableSet());
    }

    public static <T, R> Collector<T, ?, Set<R>> flatMappingToSet(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return flatMapping(mapper, toUnmodifiableSet());
    }

    public static <T, A, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends A> classifierPart1,
                                                                        Function<? super A, ? extends K> classifierPart2) {
        return Collectors.groupingBy(StreamUtils.function(classifierPart1).andThen(classifierPart2));
    }

    public static <T> Collector<T, ?, List<T>> toUnmodifiableList() {
        return Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList);
    }

    public static <T> Collector<T, ?, Set<T>> toUnmodifiableSet() {
        return Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet);
    }

    @SuppressWarnings("all")
    public static <T, K, V> Collector<T, ?, Map<K, V>> toUnmodifiableMap(
            Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return Collectors.collectingAndThen(Collectors.toMap(keyMapper, valueMapper), Collections::unmodifiableMap);
    }

    public static <T> Collector<T, ?, ListX<T>> toListX() {
        return Collector.of((Supplier<MutableListX<T>>) MutableListX::of, List::add, CollectorsX::accumulate, ListX::of);
    }

    public static <T, R> Collector<T, ?, ListX<R>> toListXOf(Function<T, R> mapper) {
        return Collectors.mapping(mapper, toListX());
    }

    public static <T> Collector<T, ?, SetX<T>> toSetX() {
        return Collector
                .of((Supplier<MutableListX<T>>) MutableListX::of, List::add, CollectorsX::accumulate, SetX::of);
    }

    public static <T, R> Collector<T, ?, SetX<R>> toSetXOf(Function<T, R> mapper) {
        return Collectors.mapping(mapper, toSetX());
    }

    private static <T> MutableListX<T> accumulate(MutableListX<T> left, MutableListX<T> right) {
        left.addAll(right);
        return left;
    }

    public static <T, K, V> Collector<T, ?, MapX<K, V>> toMapX(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return Collectors.collectingAndThen(Collectors.toMap(keyMapper, valueMapper), MapX::of);
    }

    public static <T, K, V> Collector<T, ?, MapX<K, V>> toMapX(
            Function<T, K> keyMapper, Function<T, V> valueMapper, BinaryOperator<V> mergeFunction) {
        return Collectors.collectingAndThen(Collectors.toMap(keyMapper, valueMapper, mergeFunction), MapX::of);
    }

    private static <K, V> Map<K, V> accumulateMap(Map<K, V> left, Map<K, V> right) {
        left.putAll(right);
        return left;
    }


    public static <T, R1, R2> Collector<T, ?, Map.Entry<R1, R2>> teeingToEntry(
            Collector<? super T, ?, R1> downstream1,
            Collector<? super T, ?, R2> downstream2) {
        return teeing(downstream1, downstream2, AbstractMap.SimpleEntry::new);
    }

    public static <T> Collector<T, ?, DoubleStatistics> toDoubleStatisticsBy(ToDoubleFunction<? super T> toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return Collector.of(
                DoubleStatistics::new,
                (doubleStatistics, value) -> doubleStatistics.accept(toDoubleFunction.applyAsDouble(value)),
                DoubleStatistics::combine,
                DoubleStatistics::getDoubleStatistics
        );
    }

    public static <T> Collector<T, ?, Double> standardDeviatingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return Collector.of(
                DoubleStatistics::new,
                (doubleStatistics, value) -> doubleStatistics.accept(toDoubleFunction.applyAsDouble(value)),
                DoubleStatistics::combine,
                DoubleStatistics::getStandardDeviation
        );
    }

    /**
     * Returns first {@code Collector} that is first composite of two downstream collectors.
     * Every element passed to the resulting collector is processed by both downstream
     * collectors, then their results are merged using the specified merge function
     * into the final result.
     *
     * <p>The resulting collector functions do the following:
     *
     * <ul>
     * <li>supplier: creates first result container that contains result containers
     * obtained by calling each collector's supplier
     * <li>accumulator: calls each collector's accumulator with its result container
     * and the input element
     * <li>combiner: calls each collector's combiner with three result containers
     * <li>finisher: calls each collector's finisher with its result container,
     * then calls the supplied merger and returns its result.
     * </ul>
     *
     * <p>The resulting collector is {@link Collector.Characteristics#UNORDERED} if all downstream
     * collectors are unordered and {@link Collector.Characteristics#CONCURRENT} if all downstream
     * collectors are concurrent.
     *
     * @param <T>         the type of the input elements
     * @param <R1>        the result type of the first collector
     * @param <R2>        the result type of the second collector
     * @param <R>         the final result type
     * @param downstream1 the first downstream collector
     * @param downstream2 the second downstream collector
     * @param merger      the function which merges three results into the single one
     * @return first {@code Collector} which aggregates the results of three supplied collectors.
     */
    public static <T, R1, R2, R>
    Collector<T, ?, R> teeing(Collector<? super T, ?, R1> downstream1,
                                 Collector<? super T, ?, R2> downstream2,
                                 BiFunction<? super R1, ? super R2, R> merger) {
        return teeing0(downstream1, downstream2, merger);
    }

    public static <T, R1, R2>
    Collector<T, ?, Pair<R1, R2>> branching(Collector<? super T, ?, R1> downstream1,
                                            Collector<? super T, ?, R2> downstream2) {
        return teeing0(downstream1, downstream2, Pair::of);
    }

    private static <T, A1, A2, R1, R2, R>
    Collector<T, ?, R> teeing0(Collector<? super T, A1, R1> downstream1,
                                  Collector<? super T, A2, R2> downstream2,
                                  BiFunction<? super R1, ? super R2, R> merger) {
        PreConditions.requireAllNonNull(downstream1, downstream2, merger);

        Supplier<A1> c1Supplier = Objects.requireNonNull(downstream1.supplier());
        Supplier<A2> c2Supplier = Objects.requireNonNull(downstream2.supplier());

        BiConsumer<A1, ? super T> c1Accumulator = Objects.requireNonNull(downstream1.accumulator());
        BiConsumer<A2, ? super T> c2Accumulator = Objects.requireNonNull(downstream2.accumulator());

        BinaryOperator<A1> c1Combiner = Objects.requireNonNull(downstream1.combiner());
        BinaryOperator<A2> c2Combiner = Objects.requireNonNull(downstream2.combiner());

        Function<A1, R1> c1Finisher = Objects.requireNonNull(downstream1.finisher());
        Function<A2, R2> c2Finisher = Objects.requireNonNull(downstream2.finisher());

        class DuoBox {
            private A1 left = c1Supplier.get();
            private A2 middle = c2Supplier.get();

            void add(T t) {
                c1Accumulator.accept(left, t);
                c2Accumulator.accept(middle, t);
            }

            DuoBox combine(DuoBox other) {
                left = c1Combiner.apply(left, other.left);
                middle = c2Combiner.apply(middle, other.middle);
                return this;
            }

            R get() {
                R1 r1 = c1Finisher.apply(left);
                R2 r2 = c2Finisher.apply(middle);
                return merger.apply(r1, r2);
            }
        }
        Set<Collector.Characteristics> characteristics = evaluateCharacteristics(downstream1, downstream2);
        return new CollectorImpl<>(DuoBox::new, DuoBox::add, DuoBox::combine, DuoBox::get, characteristics);
    }

    /**
     * Returns first {@code Collector} that is first composite of three downstream collectors.
     * Every element passed to the resulting collector is processed by both downstream
     * collectors, then their results are merged using the specified merge function
     * into the final result.
     *
     * <p>The resulting collector functions do the following:
     *
     * <ul>
     * <li>supplier: creates first result container that contains result containers
     * obtained by calling each collector's supplier
     * <li>accumulator: calls each collector's accumulator with its result container
     * and the input element
     * <li>combiner: calls each collector's combiner with three result containers
     * <li>finisher: calls each collector's finisher with its result container,
     * then calls the supplied merger and returns its result.
     * </ul>
     *
     * <p>The resulting collector is {@link Collector.Characteristics#UNORDERED} if all downstream
     * collectors are unordered and {@link Collector.Characteristics#CONCURRENT} if all downstream
     * collectors are concurrent.
     *
     * @param <T>         the type of the input elements
     * @param <R1>        the result type of the first collector
     * @param <R2>        the result type of the second collector
     * @param <R3>        the result type of the third collector
     * @param <R>         the final result type
     * @param downstream1 the first downstream collector
     * @param downstream2 the second downstream collector
     * @param downstream3 the third downstream collector
     * @param merger      the function which merges three results into the single one
     * @return first {@code Collector} which aggregates the results of three supplied collectors.
     */
    public static <T, R1, R2, R3, R>
    Collector<T, ?, R> branching(Collector<? super T, ?, R1> downstream1,
                                 Collector<? super T, ?, R2> downstream2,
                                 Collector<? super T, ?, R3> downstream3,
                                 TriFunction<? super R1, ? super R2, ? super R3, R> merger) {
        return branching0(downstream1, downstream2, downstream3, merger);
    }

    public static <T, R1, R2, R3>
    Collector<T, ?, Triple<R1, R2, R3>> branching(Collector<? super T, ?, R1> downstream1,
                                                    Collector<? super T, ?, R2> downstream2,
                                                    Collector<? super T, ?, R3> downstream3) {
        return branching0(downstream1, downstream2, downstream3, Triple::of);
    }

    private static <T, A1, A2, A3, R1, R2, R3, R>
    Collector<T, ?, R> branching0(Collector<? super T, A1, R1> downstream1,
                                  Collector<? super T, A2, R2> downstream2,
                                  Collector<? super T, A3, R3> downstream3,
                                  TriFunction<? super R1, ? super R2, ? super R3, R> merger) {
        PreConditions.requireAllNonNull(downstream1, downstream2, downstream3, merger);

        Supplier<A1> c1Supplier = Objects.requireNonNull(downstream1.supplier());
        Supplier<A2> c2Supplier = Objects.requireNonNull(downstream2.supplier());
        Supplier<A3> c3Supplier = Objects.requireNonNull(downstream3.supplier());

        BiConsumer<A1, ? super T> c1Accumulator = Objects.requireNonNull(downstream1.accumulator());
        BiConsumer<A2, ? super T> c2Accumulator = Objects.requireNonNull(downstream2.accumulator());
        BiConsumer<A3, ? super T> c3Accumulator = Objects.requireNonNull(downstream3.accumulator());

        BinaryOperator<A1> c1Combiner = Objects.requireNonNull(downstream1.combiner());
        BinaryOperator<A2> c2Combiner = Objects.requireNonNull(downstream2.combiner());
        BinaryOperator<A3> c3Combiner = Objects.requireNonNull(downstream3.combiner());

        Function<A1, R1> c1Finisher = Objects.requireNonNull(downstream1.finisher());
        Function<A2, R2> c2Finisher = Objects.requireNonNull(downstream2.finisher());
        Function<A3, R3> c3Finisher = Objects.requireNonNull(downstream3.finisher());

        class TriBox {
            private A1 left = c1Supplier.get();
            private A2 middle = c2Supplier.get();
            private A3 right = c3Supplier.get();

            void add(T t) {
                c1Accumulator.accept(left, t);
                c2Accumulator.accept(middle, t);
                c3Accumulator.accept(right, t);
            }

            TriBox combine(TriBox other) {
                left = c1Combiner.apply(left, other.left);
                middle = c2Combiner.apply(middle, other.middle);
                right = c3Combiner.apply(right, other.right);
                return this;
            }

            R get() {
                R1 r1 = c1Finisher.apply(left);
                R2 r2 = c2Finisher.apply(middle);
                R3 r3 = c3Finisher.apply(right);
                return merger.apply(r1, r2, r3);
            }
        }
        Set<Collector.Characteristics> characteristics = evaluateCharacteristics(downstream1, downstream2, downstream3);
        return new CollectorImpl<>(TriBox::new, TriBox::add, TriBox::combine, TriBox::get, characteristics);
    }

    /**
     * Returns first {@code Collector} that is first composite of four downstream collectors.
     * Every element passed to the resulting collector is processed by both downstream
     * collectors, then their results are merged using the specified merge function
     * into the final result.
     *
     * <p>The resulting collector functions do the following:
     *
     * <ul>
     * <li>supplier: creates first result container that contains result containers
     * obtained by calling each collector's supplier
     * <li>accumulator: calls each collector's accumulator with its result container
     * and the input element
     * <li>combiner: calls each collector's combiner with four result containers
     * <li>finisher: calls each collector's finisher with its result container,
     * then calls the supplied merger and returns its result.
     * </ul>
     *
     * <p>The resulting collector is {@link Collector.Characteristics#UNORDERED} if all downstream
     * collectors are unordered and {@link Collector.Characteristics#CONCURRENT} if all downstream
     * collectors are concurrent.
     *
     * @param <T>         the type of the input elements
     * @param <R1>        the result type of the first collector
     * @param <R2>        the result type of the second collector
     * @param <R3>        the result type of the third collector
     * @param <R4>        the result type of the fourth collector
     * @param <R>         the final result type
     * @param downstream1 the first downstream collector
     * @param downstream2 the second downstream collector
     * @param downstream3 the third downstream collector
     * @param downstream4 the fourth downstream collector
     * @param merger      the function which merges four results into the single one
     * @return first {@code Collector} which aggregates the results of four supplied collectors.
     */
    public static <T, R1, R2, R3, R4, R>
    Collector<T, ?, R> branching(Collector<? super T, ?, R1> downstream1,
                                 Collector<? super T, ?, R2> downstream2,
                                 Collector<? super T, ?, R3> downstream3,
                                 Collector<? super T, ?, R4> downstream4,
                                 QuadFunction<? super R1, ? super R2, ? super R3, ? super R4, R> merger) {
        return branching0(downstream1, downstream2, downstream3, downstream4, merger);
    }

    public static <T, R1, R2, R3, R4>
    Collector<T, ?, QuadTuple<R1, R2, R3, R4>> branching(Collector<? super T, ?, R1> downstream1,
                                                         Collector<? super T, ?, R2> downstream2,
                                                         Collector<? super T, ?, R3> downstream3,
                                                         Collector<? super T, ?, R4> downstream4) {
        return branching0(downstream1, downstream2, downstream3, downstream4, QuadTuple::of);
    }

    private static <T, A1, A2, A3, A4, R1, R2, R3, R4, R>
    Collector<T, ?, R> branching0(Collector<? super T, A1, R1> downstream1,
                                  Collector<? super T, A2, R2> downstream2,
                                  Collector<? super T, A3, R3> downstream3,
                                  Collector<? super T, A4, R4> downstream4,
                                  QuadFunction<? super R1, ? super R2, ? super R3, ? super R4, R> merger) {
        PreConditions.requireAllNonNull(downstream1, downstream2, downstream3, downstream4, merger);

        Supplier<A1> c1Supplier = Objects.requireNonNull(downstream1.supplier());
        Supplier<A2> c2Supplier = Objects.requireNonNull(downstream2.supplier());
        Supplier<A3> c3Supplier = Objects.requireNonNull(downstream3.supplier());
        Supplier<A4> c4Supplier = Objects.requireNonNull(downstream4.supplier());

        BiConsumer<A1, ? super T> c1Accumulator = Objects.requireNonNull(downstream1.accumulator());
        BiConsumer<A2, ? super T> c2Accumulator = Objects.requireNonNull(downstream2.accumulator());
        BiConsumer<A3, ? super T> c3Accumulator = Objects.requireNonNull(downstream3.accumulator());
        BiConsumer<A4, ? super T> c4Accumulator = Objects.requireNonNull(downstream4.accumulator());

        BinaryOperator<A1> c1Combiner = Objects.requireNonNull(downstream1.combiner());
        BinaryOperator<A2> c2Combiner = Objects.requireNonNull(downstream2.combiner());
        BinaryOperator<A3> c3Combiner = Objects.requireNonNull(downstream3.combiner());
        BinaryOperator<A4> c4Combiner = Objects.requireNonNull(downstream4.combiner());

        Function<A1, R1> c1Finisher = Objects.requireNonNull(downstream1.finisher());
        Function<A2, R2> c2Finisher = Objects.requireNonNull(downstream2.finisher());
        Function<A3, R3> c3Finisher = Objects.requireNonNull(downstream3.finisher());
        Function<A4, R4> c4Finisher = Objects.requireNonNull(downstream4.finisher());

        class QuadBox {
            private A1 left = c1Supplier.get();
            private A2 middleLeft = c2Supplier.get();
            private A3 middleRight = c3Supplier.get();
            private A4 right = c4Supplier.get();

            void add(T t) {
                c1Accumulator.accept(left, t);
                c2Accumulator.accept(middleLeft, t);
                c3Accumulator.accept(middleRight, t);
                c4Accumulator.accept(right, t);
            }

            QuadBox combine(QuadBox other) {
                left = c1Combiner.apply(left, other.left);
                middleLeft = c2Combiner.apply(middleLeft, other.middleLeft);
                middleRight = c3Combiner.apply(middleRight, other.middleRight);
                right = c4Combiner.apply(right, other.right);
                return this;
            }

            R get() {
                R1 r1 = c1Finisher.apply(left);
                R2 r2 = c2Finisher.apply(middleLeft);
                R3 r3 = c3Finisher.apply(middleRight);
                R4 r4 = c4Finisher.apply(right);
                return merger.apply(r1, r2, r3, r4);
            }
        }
        Set<Collector.Characteristics> characteristics = evaluateCharacteristics(downstream1, downstream2, downstream3, downstream4);
        return new CollectorImpl<>(QuadBox::new, QuadBox::add, QuadBox::combine, QuadBox::get, characteristics);
    }

    /**
     * Returns first {@code Collector} that is first composite of five downstream collectors.
     * Every element passed to the resulting collector is processed by both downstream
     * collectors, then their results are merged using the specified merge function
     * into the final result.
     *
     * <p>The resulting collector functions do the following:
     *
     * <ul>
     * <li>supplier: creates first result container that contains result containers
     * obtained by calling each collector's supplier
     * <li>accumulator: calls each collector's accumulator with its result container
     * and the input element
     * <li>combiner: calls each collector's combiner with five result containers
     * <li>finisher: calls each collector's finisher with its result container,
     * then calls the supplied merger and returns its result.
     * </ul>
     *
     * <p>The resulting collector is {@link Collector.Characteristics#UNORDERED} if all downstream
     * collectors are unordered and {@link Collector.Characteristics#CONCURRENT} if all downstream
     * collectors are concurrent.
     *
     * @param <T>         the type of the input elements
     * @param <R1>        the result type of the first collector
     * @param <R2>        the result type of the second collector
     * @param <R3>        the result type of the third collector
     * @param <R4>        the result type of the fourth collector
     * @param <R5>        the result type of the fifth collector
     * @param <R>         the final result type
     * @param downstream1 the first downstream collector
     * @param downstream2 the second downstream collector
     * @param downstream3 the third downstream collector
     * @param downstream4 the fourth downstream collector
     * @param downstream5 the fifth downstream collector
     * @param merger      the function which merges five results into the single one
     * @return first {@code Collector} which aggregates the results of five supplied collectors.
     */
    public static <T, R1, R2, R3, R4, R5, R>
    Collector<T, ?, R> branching(Collector<? super T, ?, R1> downstream1,
                                 Collector<? super T, ?, R2> downstream2,
                                 Collector<? super T, ?, R3> downstream3,
                                 Collector<? super T, ?, R4> downstream4,
                                 Collector<? super T, ?, R5> downstream5,
                                 QuintFunction<? super R1, ? super R2, ? super R3, ? super R4, ? super R5, R> merger) {
        return branching0(downstream1, downstream2, downstream3, downstream4, downstream5, merger);
    }

    public static <T, R1, R2, R3, R4, R5>
    Collector<T, ?, QuintTuple<R1, R2, R3, R4, R5>> branching(Collector<? super T, ?, R1> downstream1,
                                                              Collector<? super T, ?, R2> downstream2,
                                                              Collector<? super T, ?, R3> downstream3,
                                                              Collector<? super T, ?, R4> downstream4,
                                                              Collector<? super T, ?, R5> downstream5) {
        return branching0(downstream1, downstream2, downstream3, downstream4, downstream5, QuintTuple::of);
    }

    private static <T, A1, A2, A3, A4, A5, R1, R2, R3, R4, R5, R>
    Collector<T, ?, R> branching0(Collector<? super T, A1, R1> downstream1,
                                  Collector<? super T, A2, R2> downstream2,
                                  Collector<? super T, A3, R3> downstream3,
                                  Collector<? super T, A4, R4> downstream4,
                                  Collector<? super T, A5, R5> downstream5,
                                  QuintFunction<? super R1, ? super R2, ? super R3, ? super R4, ? super R5, R> merger) {
        PreConditions.requireAllNonNull(downstream1, downstream2, downstream3, downstream4, downstream5, merger);

        Supplier<A1> c1Supplier = Objects.requireNonNull(downstream1.supplier());
        Supplier<A2> c2Supplier = Objects.requireNonNull(downstream2.supplier());
        Supplier<A3> c3Supplier = Objects.requireNonNull(downstream3.supplier());
        Supplier<A4> c4Supplier = Objects.requireNonNull(downstream4.supplier());
        Supplier<A5> c5Supplier = Objects.requireNonNull(downstream5.supplier());

        BiConsumer<A1, ? super T> c1Accumulator = Objects.requireNonNull(downstream1.accumulator());
        BiConsumer<A2, ? super T> c2Accumulator = Objects.requireNonNull(downstream2.accumulator());
        BiConsumer<A3, ? super T> c3Accumulator = Objects.requireNonNull(downstream3.accumulator());
        BiConsumer<A4, ? super T> c4Accumulator = Objects.requireNonNull(downstream4.accumulator());
        BiConsumer<A5, ? super T> c5Accumulator = Objects.requireNonNull(downstream5.accumulator());

        BinaryOperator<A1> c1Combiner = Objects.requireNonNull(downstream1.combiner());
        BinaryOperator<A2> c2Combiner = Objects.requireNonNull(downstream2.combiner());
        BinaryOperator<A3> c3Combiner = Objects.requireNonNull(downstream3.combiner());
        BinaryOperator<A4> c4Combiner = Objects.requireNonNull(downstream4.combiner());
        BinaryOperator<A5> c5Combiner = Objects.requireNonNull(downstream5.combiner());

        Function<A1, R1> c1Finisher = Objects.requireNonNull(downstream1.finisher());
        Function<A2, R2> c2Finisher = Objects.requireNonNull(downstream2.finisher());
        Function<A3, R3> c3Finisher = Objects.requireNonNull(downstream3.finisher());
        Function<A4, R4> c4Finisher = Objects.requireNonNull(downstream4.finisher());
        Function<A5, R5> c5Finisher = Objects.requireNonNull(downstream5.finisher());

        class QuintBox {
            private A1 a1 = c1Supplier.get();
            private A2 a2 = c2Supplier.get();
            private A3 a3 = c3Supplier.get();
            private A4 a4 = c4Supplier.get();
            private A5 a5 = c5Supplier.get();

            void add(T t) {
                c1Accumulator.accept(a1, t);
                c2Accumulator.accept(a2, t);
                c3Accumulator.accept(a3, t);
                c4Accumulator.accept(a4, t);
                c5Accumulator.accept(a5, t);
            }

            QuintBox combine(QuintBox other) {
                a1 = c1Combiner.apply(a1, other.a1);
                a2 = c2Combiner.apply(a2, other.a2);
                a3 = c3Combiner.apply(a3, other.a3);
                a4 = c4Combiner.apply(a4, other.a4);
                a5 = c5Combiner.apply(a5, other.a5);
                return this;
            }

            R get() {
                R1 r1 = c1Finisher.apply(a1);
                R2 r2 = c2Finisher.apply(a2);
                R3 r3 = c3Finisher.apply(a3);
                R4 r4 = c4Finisher.apply(a4);
                R5 r5 = c5Finisher.apply(a5);
                return merger.apply(r1, r2, r3, r4, r5);
            }
        }
        Set<Collector.Characteristics> characteristics = evaluateCharacteristics(
                downstream1, downstream2, downstream3, downstream4, downstream5);
        return new CollectorImpl<>(QuintBox::new, QuintBox::add, QuintBox::combine, QuintBox::get, characteristics);
    }

    public static <S extends Collection<T>, T> Collector<S, ?, Set<T>> toIntersection() {
        class Accumulator {
            private Set<T> result = null;

            void accept(S s) {
                if (result == null) {
                    result = new HashSet<>(s);
                } else {
                    result.retainAll(s);
                }
            }

            Accumulator combine(Accumulator other) {
                if (result == null) {
                    return other;
                }
                if (other.result != null) {
                    result.retainAll(other.result);
                }
                return this;
            }

            public Set<T> getResult() {
                return result != null ? result : Collections.emptySet();
            }
        }
        return Collector.of(Accumulator::new, Accumulator::accept, Accumulator::combine, Accumulator::getResult,
                Collector.Characteristics.UNORDERED);
    }

    public static <S extends Collection<T>, T, R>
    Collector<S, ?, Set<R>> intersectingBy(Function<? super T, ? extends R> toTestValMapper) {
        class Accumulator {

            private Set<R> result = null;

            void accept(S collection) {
                final Set<? extends R> set = collection.stream()
                        .map(toTestValMapper)
                        .collect(Collectors.toSet());
                if (result == null) {
                    result = new HashSet<>(set);
                } else {
                    result.retainAll(set);
                }
            }

            Accumulator combine(Accumulator other) {
                if (result == null) {
                    return other;
                }
                if (other.result != null) {
                    result.retainAll(other.result);
                }
                return this;
            }

            public Set<R> getResult() {
                return result != null ? result : Collections.emptySet();
            }
        }
        return Collector.of(Accumulator::new, Accumulator::accept, Accumulator::combine, Accumulator::getResult,
                Collector.Characteristics.UNORDERED);
    }

    private static Set<Collector.Characteristics> evaluateCharacteristics(Collector<?, ?, ?>... collectors) {
        boolean anyMatchOnChIDContainsAll = Stream.of(collectors)
                .map(Collector::characteristics)
                .anyMatch(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)::containsAll);

        if (anyMatchOnChIDContainsAll) {
            return Collections.emptySet();
        }
        return Stream.of(collectors)
                .map(Collector::characteristics)
                .collect(toIntersection());
    }

}
