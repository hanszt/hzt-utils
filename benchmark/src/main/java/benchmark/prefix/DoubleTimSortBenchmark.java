package benchmark.prefix;

import org.hzt.utils.It;
import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.DoubleMutableList;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.ranges.IntRange;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class DoubleTimSortBenchmark {

    private static final Random RANDOM = new Random(0);
    public static final String LIST_SIZE_AS_STRING = "100000";
    private static final int LIST_SIZE = Integer.parseInt(LIST_SIZE_AS_STRING);

    @Param({LIST_SIZE_AS_STRING})
    private int nrOfIterations;

    @SuppressWarnings({"squid:S1612", "Convert2MethodRef"})
    final MutableListX<Double> inputList = IntRange.of(0, LIST_SIZE)
            .mapToObj(It::asDouble)
            .intersperse(() -> RANDOM.nextDouble())
            .toMutableList();

    final DoubleMutableList primitiveList = inputList.mapToDouble(It::asDouble).toMutableList();

    final double[] array = primitiveList.toArray();

    public DoubleTimSortBenchmark() {
        super();
    }

    @Benchmark
    public DoubleMutableList doubleListSort() {
        final var list = DoubleMutableList.of(primitiveList);
        list.sort();
        return list;
    }

    @Benchmark
    @SuppressWarnings("squid:S2384")
    public MutableListX<Double> listSort() {
        final var list = MutableListX.of(inputList);
        list.sort(Comparator.comparing(It::self));
        return list;
    }

    @Benchmark
    @SuppressWarnings("squid:S2384")
    public double[] arraySort() {
        var copy = Arrays.copyOf(array, array.length);
        Arrays.sort(copy);
        return copy;
    }

    @Benchmark
    @SuppressWarnings("squid:S2384")
    public double[] primitiveArraySortReversed() {
        var copy = Arrays.copyOf(array, array.length);
        ArraysX.sort(DoubleComparator.reverseOrder(), copy);
        return copy;
    }

    @Benchmark
    public DoubleMutableList doubleListSortReversed() {
        final var list = DoubleMutableList.of(primitiveList);
        list.sort(DoubleComparator.reverseOrder());
        return list;
    }

    @Benchmark
    @SuppressWarnings("squid:S2384")
    public MutableListX<Double> listSortReversed() {
        final var list = MutableListX.of(inputList);
        list.sort(Comparator.reverseOrder());
        return list;
    }

    public static void main(String[] args) {
        var options = new OptionsBuilder()
                .include(DoubleTimSortBenchmark.class.getSimpleName())
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
