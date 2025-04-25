package org.hzt.demo;

import org.hzt.utils.collectors.Collectors;
import org.hzt.utils.function.Consumer;
import org.hzt.utils.function.Function;
import org.hzt.utils.function.Predicate;
import org.hzt.utils.iterators.UnmodifiableIterator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.SequenceExtensions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.hzt.demo.function.IntegerFunctions.*;
import static org.hzt.demo.function.StringFunctions.plusStringLength;
import static org.hzt.demo.function.StringFunctions.toStringLength;
import static org.hzt.utils.collectors.Collectors.*;
import static org.hzt.utils.function.Functions.*;

public final class Launcher {

    private static final Predicate<Integer> isEven = new Predicate<Integer>() {
        public boolean test(final Integer s) {
            return s % 2 == 0;
        }
    };

    private static final Function<String, Integer> toCharCode = new Function<String, Integer>() {
        public Integer apply(final String s) {
            return (int) s.charAt(0);
        }
    };
    private static final Consumer<Object> println = new Consumer<Object>() {

        public void accept(final Object o) {
            System.out.println(o);
        }
    };

    private static Consumer<Integer> sleep(final int millis) {
        return new Consumer<Integer>() {

            public void accept(final Integer integer) {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }

    public static void main(String[] args) {
        final Sequence<String> letters = Sequence.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");

        System.out.println("Map filter to list demo");
        final List<Integer> charCodes = letters
                .map(toCharCode)
                .filter(isEven.or(greaterThan(104)))
                .toList();
        System.out.println("list = " + charCodes);

        System.out.println("None demo");
        System.out.println(letters.none(Predicate.<String>isEqual("a")));

        System.out.println("Any demo");
        System.out.println(Sequence.of(charCodes).any(lessThan(42)));
        // First or null demo
        System.out.println("First demo");
        System.out.println(letters
                .map(toCharCode)
                .filter(isEven.and(greaterThanEqual(100)))
                .first());

        iterateDemo();
        System.out.println("Fold demo");
        System.out.println(letters.fold(1, plusStringLength.andThen(times(2))));
        flatMapDemo();
        groupByDemoCounting();
        groupByDemoSumming();

        System.out.println("Summing demo");
        System.out.println(Sequence.iterate(10, plus(1))
                .take(4)
                .collect(sumOf(Function.<Integer>identity())));

        chunkedDemo();
        windowedDemo();
    }

    private static void chunkedDemo() {
        System.out.println("chunkedDemo");
        final Sequence.Builder<Integer> builder = Sequence.builder();
        gcdByEuclidesAlgorithm(10230, 612, builder);
        for (final List<Integer> integers : builder.build().andThen(SequenceExtensions.<Integer>chunked(3))) {
            System.out.println(integers);
        }
    }

    private static void windowedDemo() {
        System.out.println("windowedDemo");
        for (final List<Integer> integers : Sequence.iterate(1, plus(1))
                .take(22)
                .andThen(SequenceExtensions.<Integer>windowed(3, 2, true))) {
            System.out.println(integers);
        }
    }

    public static int gcdByEuclidesAlgorithm(int n1, int n2, Sequence.Builder<Integer> builder) {
        builder.add(n2);
        return n2 == 0 ? n1 : gcdByEuclidesAlgorithm(n2, n1 % n2, builder);
    }

    private static void flatMapDemo() {
        System.out.println("flatMapDemo");
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
        System.out.println("groupByDemoCounting");
        final Map<Integer, Long> grouping = Sequence.of("This", "is", "an", "example")
                .collect(groupBy(toStringLength, Collectors.<String>count()));
        System.out.println(grouping);
    }

    private static void groupByDemoSumming() {
        System.out.println("groupByDemoSumming");
        final Map<Integer, Integer> grouping = Sequence.of("This", "is", "an", "example", "text")
                .collect(groupBy(toStringLength, sumOf(toStringLength)));
        System.out.println(grouping);
    }

    private static void iterateDemo() {
        System.out.println("iterateDemo");
        final Function<Integer, Integer> powerOf2Mod30 = new Function<Integer, Integer>() {
            public Integer apply(final Integer exp) {
                return (int) Math.pow(2, exp % 30);
            }
        };
        final Sequence<Integer> sequence = Sequence.iterate(0, plus(1))
                .map(powerOf2Mod30)
                .skip(2)
                .take(3);

        sequence.forEach(sleep(100).andThen(println));

        System.out.println("Reduced: " + sequence.reduce(sum));
    }

    private static Iterable<Character> toChars(final String s) {
        final int length = s.length();
        return new Iterable<Character>() {

            public Iterator<Character> iterator() {
                return new UnmodifiableIterator<Character>() {
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
}
