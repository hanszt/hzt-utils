package benchmark.prefix;

import org.hzt.utils.numbers.IntX;
import org.hzt.utils.streams.StreamX;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class PrefixStreamXMapFilterToListBenchmark {
    public static final String NR_OF_ITERATIONS = "1000000";

    @Param({NR_OF_ITERATIONS})
    private int nrOfIterations;

    private final List<String> list = IntStream.range(0, Integer.parseInt(NR_OF_ITERATIONS))
            .mapToObj(String::valueOf)
            .toList();

    public PrefixStreamXMapFilterToListBenchmark() {
        super();
    }

    @Benchmark
    public List<Integer> streamMapFilterToList() {
        return list.stream()
                .map(String::length)
                .filter(IntX::isEven)
                .toList();
    }

    @Benchmark
    public List<Integer> parallelStreamMapFilterToList() {
        return list.parallelStream()
                .map(String::length)
                .filter(IntX::isEven)
                .toList();
    }

    @Benchmark
    public List<Integer> streamXMapFilterToList() {
        return StreamX.of(list)
                .map(String::length)
                .filter(IntX::isEven)
                .toList();
    }

    @Benchmark
    public List<Integer> parallelStreamXMapFilterToList() {
        return StreamX.parallel(list)
                .map(String::length)
                .filter(IntX::isEven)
                .toList();
    }

    @Benchmark
    public List<Integer> imperativeMapFilterToList() {
        final List<Integer> result = new ArrayList<>();
        for (final var s : list) {
            final var length = s.length();
            if (IntX.isEven(length)) {
                result.add(length);
            }
        }
        return List.copyOf(result);
    }

    public static void main(final String[] args) {
        final var options = new OptionsBuilder()
                .include(PrefixStreamXMapFilterToListBenchmark.class.getSimpleName())
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
