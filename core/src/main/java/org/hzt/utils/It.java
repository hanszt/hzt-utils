package org.hzt.utils;

/**
 * <p>
 * 'It' is a class to easily refer to itself in a method reference and for some other utility functions such as
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
@SuppressWarnings({"squid:S106", "squid:S1172"})
public final class It {

    private It() {
    }

    public static <T> T self(final T t) {
        return t;
    }

    public static int asInt(final int i) {
        return i;
    }

    public static int doubleAsInt(final double d) {
        return (int) d;
    }

    public static int longAsInt(final long l) {
        return (int) l;
    }

    public static long doubleAsLong(final double d) {
        return (long) d;
    }

    public static long asLong(final long l) {
        return l;
    }

    public static double asDouble(final double d) {
        return d;
    }

    @SuppressWarnings("SameReturnValue")
    public static <T> boolean noFilter(@SuppressWarnings("unused") final T t) {
        return true;
    }

    @SuppressWarnings("SameReturnValue")
    public static boolean noIntFilter(@SuppressWarnings("unused") final int t) {
        return true;
    }

    @SuppressWarnings("SameReturnValue")
    public static boolean noLongFilter(@SuppressWarnings("unused") final long t) {
        return true;
    }

    @SuppressWarnings("SameReturnValue")
    public static boolean noDoubleFilter(@SuppressWarnings("unused") final double t) {
        return true;
    }

    @SuppressWarnings("SameReturnValue")
    public static <T> boolean blockingFilter(@SuppressWarnings("unused") final T t) {
        return false;
    }

    public static void println() {
        System.out.println();
    }

    public static <T> void println(final T value) {
        System.out.println(value);
    }

    public static <T> void print(final T value) {
        System.out.print(value);
    }

    public static void printf(final String format, final Object... values) {
        System.out.printf(format, values);
    }

    public static <T> boolean notEquals(final T t1, final T t2) {
        return !t1.equals(t2);
    }
}
