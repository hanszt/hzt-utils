package benchmark.prefix;

import org.hzt.utils.numbers.IntX;
import org.hzt.utils.sequences.Sequence;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class PrefixSequenceMapFilterReduceBenchmark {

    public static final String LIST_SIZE = "100000";

    @Param({LIST_SIZE})
    private int nrOfIterations;

    private final List<String> list = IntStream.range(0, Integer.parseInt(LIST_SIZE))
            .mapToObj(String::valueOf)
            .collect(Collectors.toUnmodifiableList());

    public PrefixSequenceMapFilterReduceBenchmark() {
        super();
    }

    @Benchmark
    public int sequenceOfListMapFilterReduce() {
        return Sequence.of(list)
                .map(String::length)
                .filter(IntX::isEven)
                .reduce(1, (a, b) -> a * b);
    }

    @Benchmark
    public int streamMapFilterReduce() {
        return list.stream()
                .map(String::length)
                .filter(IntX::isEven)
                .reduce(1, (a, b) -> a * b);
    }

    @Benchmark
    public int parallelStreamMapFilterReduce() {
        return list.parallelStream()
                .map(String::length)
                .filter(IntX::isEven)
                .reduce(1, (a, b) -> a * b);
    }

    @Benchmark
    public int imperativeMapFilterReduce() {
        var product = 1;
        for (final var s : list) {
            final var length = s.length();
            if (IntX.isEven(length)) {
                product *= length;
            }
        }
        return product;
    }

    public static void main(final String[] args) {
        final var options = new OptionsBuilder()
                .include(PrefixSequenceMapFilterReduceBenchmark.class.getSimpleName())
                .forks(2)
                .warmupIterations(2)
                .measurementIterations(3)
                .shouldFailOnError(true)
                .build();
        try {
            new Runner(options).run();
        } catch (final RunnerException e) {
            throw new IllegalStateException(e);
        }
    }
}
