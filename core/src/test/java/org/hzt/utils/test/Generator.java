package org.hzt.utils.test;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.test.model.PaintingAuction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hzt.utils.It.println;

public final class Generator {

    private Generator() {
    }

    public static MutableListX<PaintingAuction> createAuctions() {

        final var groupedByLastName = paintingsByName();
        final var vanGoghPaintings = groupedByLastName.get("van Gogh");
        final var vermeerPaintings = groupedByLastName.get("Vermeer");
        final var picassoPaintings = groupedByLastName.get("Picasso");

        final var painter = new Painter("Hans", "Zuidervaart", LocalDate.of(1989, 10, 18));
        return MutableListX.of(
                new PaintingAuction("Van Gogh Auction", LocalDate.of(1992, Month.APRIL, 2), vanGoghPaintings),
                new PaintingAuction("Vermeer Auction", LocalDate.of(1940, Month.JANUARY, 23), vermeerPaintings),
                new PaintingAuction("Picasso Auction", LocalDate.of(1965, Month.AUGUST, 4), picassoPaintings),
                new PaintingAuction(null, null, List.of(new Painting("Test", painter, Year.of(2000), false))));
    }

    private static Map<String, List<Painting>> paintingsByName() {
        return TestSampleGenerator.createPaintingList().stream()
                .collect(Collectors.groupingBy(painting -> painting.painter().getLastname()));
    }

    public static PaintingAuction createVanGoghAuction() {
        final var groupedByLastName = paintingsByName();
        final var vanGoghPaintings = groupedByLastName.get("van Gogh");
        return new PaintingAuction("Van Gogh Auction", LocalDate.of(1992, Month.APRIL, 2), vanGoghPaintings);
    }

    public static String printAndReturnAsString(final int integer) {
        final var s = "val " + integer;
        println(s);
        return s;
    }

    public static long fib(final long n) {
        long first = 0;
        long next = 1;
        if (n == 0) {
            return first;
        }
        for (var i = 2; i <= n; i++) {
            final var temp = first + next;
            first = next;
            next = temp;
        }
        return next;
    }

    public static long fibSum(final int n) {
        if (n <= 0) {
            return 0;
        }
        final var fib = new long[n + 1];
        fib[0] = 0;
        fib[1] = 1;

        // Initialize result
        var sum = fib[0] + fib[1];

        // Add remaining terms
        for (var i = 2; i <= n; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
            sum += fib[i];
        }
        return sum;
    }

    public static BigDecimal fibSumBd(final int n) {
        if (n <= 0) {
            return BigDecimal.ZERO;
        }
        var forLast = BigDecimal.ZERO;
        var last = BigDecimal.ONE;

        // Initialize result
        var sum = forLast.add(last);

        // Add remaining terms
        for (var i = 2; i <= n; i++) {
            final var next = last.add(forLast);
            sum = sum.add(next);
            forLast = last;
            last = next;
        }
        return sum;
    }

    public static int sawTooth(int value) {
        return value >= 20 ? 1 : ++value;
    }

}
