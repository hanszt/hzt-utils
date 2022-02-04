package hzt.utils;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static java.lang.System.*;

/**
 * <p>
 * 'It' is a class to easely refer to iteself in a method reference and for some other utility functions such as
 * println()
 * <pre>{@code
 *                final var museumMap = museumList.stream()
 *                 .collect(toMap(It::self, Museum::getMostPopularPainting));
 *
 * }</pre>
 * <p>
 * It can also be used to make a function explicit, and then call default functions on it. In this case a consumer:
 * <pre>{@code
 *                          List<Integer> integers = new ArrayList<>();
 *
 *                         var consumer = It.<Consumer<Integer>>self(It::println)
 *                         .andThen(integers::add));
 *
 *                         consumer.accept(3);
 * }</pre>
 *
 * This consumer now adds an item to the list and also prints out the added value. It is now a combined consumer
 * in this case, a type witness needs to be used to declare the type
 *
 */
public final class It {

    private It() {
    }

    public static <T> T self(T t) {
        return t;
    }

    public static int asInt(int i) {
        return i;
    }

    public static long asLong(long l) {
        return l;
    }

    public static double asDouble(double d) {
        return d;
    }

    public static IntPredicate noIntFilter() {
        return t -> true;
    }

    public static <T> Predicate<T> noFilter() {
        return t -> true;
    }

    public static IntPredicate blockingIntFilter() {
        return t -> false;
    }

    public static <T> Predicate<T> blockingFilter() {
        return t -> false;
    }

    public static <T> void println(T value) {
        out.println(value);
    }

    public static <T> void print(T value) {
        out.print(value);
    }
}
