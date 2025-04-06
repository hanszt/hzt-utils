package org.hzt;

import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.function.BiFunction;
import org.hzt.utils.function.Consumer;
import org.hzt.utils.function.Function;
import org.hzt.utils.function.Predicate;
import org.hzt.utils.iterators.AbstractIterator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.SequenceExtensions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.hzt.utils.collectors.CollectorsX.*;

public final class Launcher {

    private static final Function<String, Integer> stringLength = new Function<String, Integer>() {
        public Integer apply(final String i) {
            return i.length();
        }
    };

    private static final Function<Integer, Integer> increment = new Function<Integer, Integer>() {
        public Integer apply(final Integer i) {
            return i + 1;
        }
    };

    private static <T> Function<T, T> identity() {
        return new Function<T, T>() {
            public T apply(final T t) {
                return t;
            }
        };
    }

    public static void main(String[] args) {
        final Sequence<String> sequence = Sequence.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");

        final List<Integer> list = getMappedFilteredSequence(sequence).toList();
        System.out.println("list = " + list);

        System.out.println(sequence.none(new Predicate<String>() {
            public boolean test(final String s) {
                return s.equals("a");
            }
        }));

        anyDemo(list);
        // First or null demo
        System.out.println(getMappedFilteredSequence(sequence).firstOrNull());

        iterateDemo();
        foldDemo(sequence);
        flatMapDemo();
        groupByDemoCounting();
        groupByDemoSumming();
        System.out.println(Sequence.iterate(10, increment)
                .take(4)
                .collect(sumOf(Launcher.<Integer>identity())));

        chunkedDemo();
        windowedDemo();
    }

    private static void chunkedDemo() {
        System.out.println("chunkedDemo");
        final Sequence.Builder<Integer> builder = Sequence.builder();
        gcdByEuclidesAlgorithm(10230, 612, builder);
        final Sequence<List<Integer>> s = builder.build().then(SequenceExtensions.<Integer>chunked(3));
        for (final List<Integer> integers : s) {
            System.out.println(integers);
        }
    }

    private static void windowedDemo() {
        System.out.println("windowedDemo");
        final Sequence<List<Integer>> windows = Sequence.iterate(1, increment)
                .take(22)
                .then(SequenceExtensions.<Integer>windowed(3, 2, true));
        for (final List<Integer> integers : windows) {
            System.out.println(integers);
        }
    }

    public static int gcdByEuclidesAlgorithm(int n1, int n2, Sequence.Builder<Integer> builder) {
        builder.add(n2);
        return n2 == 0 ? n1 : gcdByEuclidesAlgorithm(n2, n1 % n2, builder);
    }

    private static void anyDemo(final List<Integer> list) {
        final Predicate<Integer> smallerThan42 = new Predicate<Integer>() {
            public boolean test(final Integer s) {
                return s < 42;
            }
        };
        System.out.println(Sequence.of(list).any(smallerThan42));
    }

    private static void flatMapDemo() {
        final Function<String, Iterable<Character>> toChars = new Function<String, Iterable<Character>>() {
            public Iterable<Character> apply(final String s) {
                return toChars(s);
            }
        };
        System.out.println(Sequence.of("This", "is", "an", "example")
                .flatMap(toChars)
                .collect(toSet()));
    }

    private static void groupByDemoCounting() {
        final Map<Integer, Long> grouping = Sequence.of("This", "is", "an", "example")
                .collect(groupBy(stringLength, CollectorsX.<String>count()));
        System.out.println(grouping);
    }

    private static void groupByDemoSumming() {
        final Map<Integer, Integer> grouping = Sequence.of("This", "is", "an", "example", "text")
                .collect(groupBy(stringLength, sumOf(stringLength)));
        System.out.println(grouping);
    }

    private static void foldDemo(final Sequence<String> sequence) {
        final BiFunction<Integer, String, Integer> sumOfStringLength = new BiFunction<Integer, String, Integer>() {
            public Integer apply(final Integer acc, final String s) {
                return acc + s.length();
            }
        };
        System.out.println(sequence.fold(1, sumOfStringLength));
    }

    private static void iterateDemo() {
        final Function<Integer, Integer> powerOf2Mod30 = new Function<Integer, Integer>() {
            public Integer apply(final Integer exp) {
                return (int) Math.pow(2, exp % 30);
            }
        };
        final Sequence<Integer> sequence = Sequence.iterate(0, increment)
                .map(powerOf2Mod30)
                .skip(2)
                .take(3);

        final Consumer<Integer> printAndSleep = new Consumer<Integer>() {

            public void accept(final Integer integer) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(integer);
            }
        };
        sequence.forEach(printAndSleep);

        final BiFunction<Integer, Integer, Integer> IncrementAssign = new BiFunction<Integer, Integer, Integer>() {
            public Integer apply(final Integer acc, final Integer c) {
                return acc + c;
            }
        };
        final Integer x = sequence.reduceOrNull(IncrementAssign);
        System.out.println("Reduced: " + x);
    }

    private static Iterable<Character> toChars(final String s) {
        final int length = s.length();
        return new Iterable<Character>() {

            public Iterator<Character> iterator() {
                return new AbstractIterator<Character>() {
                    int index = 0;

                    public boolean hasNext() {
                        return index < length;
                    }

                    public Character next() {
                        return s.charAt(index++);
                    }
                };
            }
        };
    }

    private static Sequence<Integer> getMappedFilteredSequence(final Sequence<String> sequence) {
        final Function<String, Integer> toCharCode = new Function<String, Integer>() {
            public Integer apply(final String s) {
                return (int) s.charAt(0);
            }
        };
        final Predicate<Integer> isEven = new Predicate<Integer>() {
            public boolean test(final Integer s) {
                return s % 2 == 0;
            }
        };
        return sequence
                .map(toCharCode)
                .filter(isEven);
    }

}
