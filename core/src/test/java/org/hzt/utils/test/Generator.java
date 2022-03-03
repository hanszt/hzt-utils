package org.hzt.utils.test;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.test.model.PaintingAuction;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Generator {

    private Generator() {
    }

    public static MutableListX<PaintingAuction> createAuctions() {

        final Map<String, List<Painting>> groupedByLastName = paintingsByName();
        final List<Painting> vanGoghPaintings = groupedByLastName.get("van Gogh");
        final List<Painting> vermeerPaintings = groupedByLastName.get("Vermeer");
        final List<Painting> picassoPaintings = groupedByLastName.get("Picasso");

        final Painter painter = new Painter("Hans", "Zuidervaart", LocalDate.of(1989, 10, 18));
        return MutableListX.of(
                new PaintingAuction("Van Gogh Auction", LocalDate.of(1992, Month.APRIL, 2), vanGoghPaintings),
                new PaintingAuction("Vermeer Auction", LocalDate.of(1940, Month.JANUARY, 23), vermeerPaintings),
                new PaintingAuction("Picasso Auction", LocalDate.of(1965, Month.AUGUST, 4), picassoPaintings),
                new PaintingAuction(null, null, Collections.singletonList(new Painting("Test", painter, Year.of(2000), false))));
    }

    private static Map<String, List<Painting>> paintingsByName() {
        return TestSampleGenerator.createPaintingList().stream()
                .collect(Collectors.groupingBy(painting -> painting.painter().getLastname()));
    }

    public static PaintingAuction createVanGoghAuction() {
        final Map<String, List<Painting>> groupedByLastName = paintingsByName();
        final List<Painting> vanGoghPaintings = groupedByLastName.get("van Gogh");
        return new PaintingAuction("Van Gogh Auction", LocalDate.of(1992, Month.APRIL, 2), vanGoghPaintings);
    }

    public static String toStringIn50Millis(int integer) {
        try {
            Thread.sleep(50);
            return "val " + integer;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "error " + integer;
        }
    }

    public static long fib(long n) {
        long first = 0;
        long next = 1;
        if (n == 0) {
            return first;
        }
        for (int i = 2; i <= n; i++) {
            long temp = first + next;
            first = next;
            next = temp;
        }
        return next;
    }

    public static long fibSum(int n) {
        if (n <= 0) {
            return 0;
        }
        long[] fib = new long[n + 1];
        fib[0] = 0;
        fib[1] = 1;

        // Initialize result
        long sum = fib[0] + fib[1];

        // Add remaining terms
        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
            sum += fib[i];
        }
        return sum;
    }

    public static BigDecimal fibSumBd(int n) {
        if (n <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal forLast = BigDecimal.ZERO;
        BigDecimal last = BigDecimal.ONE;

        // Initialize result
        BigDecimal sum = forLast.add(last);

        // Add remaining terms
        for (int i = 2; i <= n; i++) {
            BigDecimal next = last.add(forLast);
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
