package benchmark.prefix;

import org.hzt.utils.It;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.primitives.IntMutableListX;
import org.hzt.utils.numbers.LongX;
import org.hzt.utils.ranges.IntRange;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class PrefixSequenceGroupingBenchmark {

    private static final int UPPER_BOUND_RANGE = 100_000;

    public static final int GROUP_BY_VALUE = 10;
    private static final Set<Integer> fibonacciNrs = LongX.fibonacciSequence()
            .mapToObj(It::longAsInt)
            .takeWhile(i -> i >= 0)
            .toSet();

    @Param({"100000"})
    private int nrOfIterations;

    public PrefixSequenceGroupingBenchmark() {
        super();
    }

    @Benchmark
    public MapX<Integer, IntMutableListX> intRangeFilterGroup() {
        return IntRange.of(0, UPPER_BOUND_RANGE)
                .filter(fibonacciNrs::contains)
                .groupBy(i -> i % GROUP_BY_VALUE);
    }

    @Benchmark
    public Map<Integer, List<Integer>> intStreamRangeFilterGroup() {
        return IntStream.range(0, UPPER_BOUND_RANGE)
                .filter(fibonacciNrs::contains)
                .boxed()
                .collect(Collectors.groupingBy(i -> i % GROUP_BY_VALUE));
    }

    @Benchmark
    public Map<Integer, List<Integer>> parallelIntStreamRangeFilterGroup() {
        return IntStream.range(0, UPPER_BOUND_RANGE)
                .parallel()
                .filter(fibonacciNrs::contains)
                .boxed()
                .collect(Collectors.groupingBy(i -> i % GROUP_BY_VALUE));
    }

    @Benchmark
    public Map<Integer, List<Integer>> groupByImperative() {
        Map<Integer, List<Integer>> grouping = new HashMap<>();
        for (int i = 0; i < UPPER_BOUND_RANGE; i++) {
            if (fibonacciNrs.contains(i)) {
                int groupingNr = i % GROUP_BY_VALUE;
                final List<Integer> integers = grouping.computeIfAbsent(groupingNr, integer -> new ArrayList<>());
                integers.add(i);
            }
        }
        return grouping;
    }

    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(PrefixSequenceGroupingBenchmark.class.getSimpleName())
                .forks(2)
                .warmupIterations(2)
                .measurementIterations(3)
                .shouldFailOnError(true)
                .build();
        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            throw new IllegalStateException(e);
        }
    }
}
