package benchmark.prefix;

import org.hzt.utils.It;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.ranges.IntRange;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class IntTimSortBenchmark {

    private static final Random RANDOM = new Random(0);
    public static final String LIST_SIZE_AS_STRING = "100000";
    private static final int LIST_SIZE = Integer.parseInt(LIST_SIZE_AS_STRING);

    @Param({LIST_SIZE_AS_STRING})
    private int nrOfIterations;

    final MutableListX<Integer> inputList = IntRange.of(0, LIST_SIZE)
            .boxed()
            .intersperse(() -> RANDOM.nextInt(LIST_SIZE))
            .toMutableList();

    final IntMutableList primitiveList = inputList.mapToInt(It::asInt).toMutableList();

    final int[] array = primitiveList.toArray();

    public IntTimSortBenchmark() {
        super();
    }

    @Benchmark
    public IntMutableList intListSort() {
        final IntMutableList list = IntMutableList.of(primitiveList);
        list.sort();
        return list;
    }

    @Benchmark
    @SuppressWarnings("squid:S2384")
    public MutableListX<Integer> listSort() {
        final MutableListX<Integer> list = MutableListX.of(inputList);
        list.sort(Comparator.comparing(It::self));
        return list;
    }

    @Benchmark
    @SuppressWarnings("squid:S2384")
    public int[] arraySort() {
        final int[] copy = Arrays.copyOf(array, array.length);
        Arrays.sort(copy);
        return copy;
    }

    @Benchmark
    public IntMutableList intListSortReversed() {
        final IntMutableList list = IntMutableList.of(primitiveList);
        list.sort(IntComparator.reverseOrder());
        return list;
    }

    @Benchmark
    @SuppressWarnings("squid:S2384")
    public MutableListX<Integer> listSortReversed() {
        final MutableListX<Integer> list = MutableListX.of(inputList);
        list.sort(Comparator.reverseOrder());
        return list;
    }

    public static void main(final String[] args) {
        final Options options = new OptionsBuilder()
                .include(IntTimSortBenchmark.class.getSimpleName())
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
