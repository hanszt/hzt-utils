package hzt.stream;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hzt.stream.StreamUtils.*;
import static hzt.function.predicates.DateTimePredicates.isBefore;
import static hzt.function.predicates.StringPredicates.contains;
import static hzt.function.predicates.StringPredicates.containsNoneOf;
import static hzt.function.predicates.StringPredicates.hasEqualLength;
import static hzt.function.predicates.StringPredicates.startsWith;
import static java.util.Comparator.comparing;
import static java.util.function.Predicate.isEqual;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StreamUtilsTest {

    public static final int AMOUNT = 10_000_000;

    /**
     * combineAll and composeAll have exactly the same effect
     */
    @Test
    void testHigherOrderFunctionCombinerAndComposerUsingColor() {
        //arrange
        final Color originalColor = new Color(123, 32, 21);

        Function<Color, Color> combinedFilter = combine(
                Color::brighter,
                StreamUtilsTest::maxRed,
                StreamUtilsTest::removeBlue);

        Function<Color, Color> composedFilter = composeAll(
                Color::brighter,
                StreamUtilsTest::maxRed,
                StreamUtilsTest::removeBlue);
        //act
        final Color colorByCombinedFilter = combinedFilter.apply(originalColor);
        final Color colorByComposedFilter = composedFilter.apply(originalColor);

        System.out.println("originalColor = " + originalColor);
        System.out.println("colorByCombinedFilter = " + colorByCombinedFilter);
        System.out.println("colorByComposedFilter = " + colorByComposedFilter);
        //assert
        assertAll(
                () -> assertEquals(colorByCombinedFilter, colorByComposedFilter),
                () -> assertEquals(0, colorByCombinedFilter.getBlue()),
                () -> assertEquals(255, colorByCombinedFilter.getRed()),
                () -> assertTrue(originalColor.getGreen() < colorByCombinedFilter.getGreen())
        );

    }

    private static Color maxRed(Color t) {
        final int MAX_RGB = 255;
        return new Color(MAX_RGB, t.getGreen(), t.getBlue());
    }

    private static Color removeBlue(Color color) {
        return new Color(color.getRed(), color.getGreen(), 0);
    }

    @Test
    void testCombinePredicateUsingOrEvaluatesToTrue() {
        final String[] strings = new String[]{"hallo", "hoe", "gaat", "het", "met", "jou", "?"};

        final long count = Stream.of(strings)
                .filter(anyMatch(
                        contains("h"),
                        contains("e"),
                        contains("g"),
                        isEqual("jou"),
                        hasEqualLength(1)))
                .count();
        assertEquals(strings.length, count);
    }

    @Test
    void testCombinePredicateUsingOrEvaluatesToFalse() {
        assertEquals("", Stream.of("Dit is één fout", "?!", "Dan ", "is", "'t", "prima")
                .filter(anyMatch(
                        contains("h"),
                        contains("e"),
                        contains("g"),
                        isEqual("jou"),
                        hasEqualLength(1)))
                .findFirst().orElse(""));
    }

    @Test
    void testCombinePredicateUsingAndEvaluatesToEmptyString() {
        assertEquals("", Stream.of("Dit is één fout", "?!", "Dan ", "is", "'t", "prima")
                .filter(allMatch(
                        contains("h"),
                        contains("e"),
                        contains("g"),
                        isEqual("jou"),
                        hasEqualLength(1)))
                .findFirst().orElse(""));
    }

    @Test
    void testCombinePredicateUsingAndEvaluatesToFalse() {
        final String TEST = "Dit is één fout";

        final String anyString = Stream.of(TEST, "?!", "Dan ", "is", "'t", "prima")
                .filter(allMatch(
                        startsWith("D"),
                        contains("s"),
                        contains("é"),
                        e -> !e.contains("raar"),
                        hasEqualLength(TEST.length())))
                .findAny()
                .orElse("");

        assertEquals(TEST, anyString);
        assertFalse(isEqual("Hallo").or(isEqual("Raar")).test("f"));
    }

    @Test
    void testCombineComparators() {
        List<Book> bookList = TestSampleGenerator.createBookList();

        final List<Book> expected = bookList.stream()
                .sorted(comparing(Book::getCategory).reversed().thenComparing(Book::getTitle))
                .collect(Collectors.toList());

        final List<Book> actual = bookList.stream()
                .sorted(sequential(comparing(Book::getCategory).reversed(), comparing(Book::getTitle)))
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    void testComparatorsThenComparing() {
        List<Book> bookList = TestSampleGenerator.createBookList();

        final List<Book> expected = bookList.stream()
                .sorted(comparing(book -> book.getCategory() + book.getTitle()))
                .collect(Collectors.toList());

        final List<Book> actual = bookList.stream()
                .sorted(comparing(Book::getCategory).thenComparing(Book::getTitle))
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    void testMappingBeforeFiltering() {
        List<Book> books = TestSampleGenerator.createBookList();
        final String E = "e";
        final String A = "u";

        final List<Book> expected = books.stream()
                .filter(book -> !(book.getTitle().contains(E) || book.getTitle().contains(A)))
                .collect(Collectors.toList());

        final List<Book> filteredBookList = books.stream()
                .filter(by(Book::getTitle, containsNoneOf(E, A)))
                .collect(Collectors.toList());

        filteredBookList.forEach(System.out::println);

        assertEquals(expected, filteredBookList);
    }

    @Test
    void testCompose() {
        List<Book> books = TestSampleGenerator.createBookList();

        final List<Field> fieldListOfObjectClass = books.stream()
                .map(function(Object::getClass)
                        .compose(Book::getCategory)
                        .andThen(Class::getDeclaredFields))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

        fieldListOfObjectClass.forEach(System.out::println);

        assertFalse(fieldListOfObjectClass.isEmpty());
    }

    @Test
    void testChainingAPredicate() {
        List<Book> books = TestSampleGenerator.createBookList();

        final List<Book> filteredBookList = books.stream()
                .filter(by(Book::hasCopies).or(Book::isAboutProgramming))
                .collect(Collectors.toList());

        filteredBookList.forEach(System.out::println);

        assertEquals(2, filteredBookList.size());
    }

    @Test
    void testChainingMultipleBy() {
        List<Book> books = TestSampleGenerator.createBookList();

        final List<Book> expected = books.stream()
                .filter(book -> book.getTitle().startsWith("t") && book.getCategory().contains("2"))
                .collect(Collectors.toList());

        final List<Book> filteredBookList = books.stream()
                .filter(by(Book::getTitle, startsWith("t")).and(by(Book::getCategory, contains("2"))))
                .collect(Collectors.toList());

        filteredBookList.forEach(System.out::println);

        assertEquals(expected, filteredBookList);
    }

    @Test
    void testCastIfInstanceUsingFlatMap() {
        final BigDecimal bigDecimal = BigDecimal.valueOf(3);
        List<Number> numbers = Arrays.asList(3D, 3, 4F, 5.4, 6, bigDecimal, null);

        final List<Double> actual = numbers.stream()
                .flatMap(castIfInstance(Double.class))
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(3D, 5.4), actual);
    }

    @Test
    void testNumberCastIfInstanceUsingFlatMap() {
        List<Number> numbers = TestSampleGenerator.createRandomNumberTypeList(AMOUNT);

        final List<Double> expected = numbers.stream()
                .filter(Double.class::isInstance)
                .map(Double.class::cast)
                .collect(Collectors.toList());

        final List<Double> actual = numbers.stream()
                .flatMap(castIfInstance(Double.class))
                .collect(Collectors.toList());

        System.out.println("numbers.size() = " + numbers.size());
        System.out.println("actual.size() = " + actual.size());

        assertEquals(expected, actual);
    }

    @Test
    void testMappedConsumer() {
        //arrange
        final List<Book> bookList = TestSampleGenerator.createBookList();
        //act
        bookList.forEach(transformAndThen(Book::getCategory, System.out::println));
        //assert
        assertFalse(bookList.isEmpty());
    }

    @Test
    void testNestedNonNullCheck() {
        List<Painting> listContainingNestedNulls = Arrays.asList(
                new Painting("", null, null, false),
                null);

        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();
        final List<Painting> concatenatedList = Stream.concat(listContainingNestedNulls.stream(), paintingList.stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("concatenatedList = " + concatenatedList);

        final List<Painting> paintings = concatenatedList.stream()
                .filter(nonNull(Painting::painter))
                .collect(Collectors.toList());

        assertEquals(paintingList, paintings);
    }

    @Test
    void testNestedTwoLevelsDeepNonNullCheck() {
        List<Painting> listContainingNestedNulls = getPaintingListContainingNestedNulls();

        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();
        final List<Painting> concatenatedList = Stream.concat(listContainingNestedNulls.stream(), paintingList.stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("concatenatedList = " + concatenatedList);

        final List<Painting> containingNullsFilteredOutList = concatenatedList.stream()
                .filter(nonNull(Painting::painter, Painter::getDateOfBirth))
                .collect(Collectors.toList());

        assertEquals(paintingList, containingNullsFilteredOutList);
    }

    private static List<Painting> getPaintingListContainingNestedNulls() {
        return Arrays.asList(
                new Painting("", new Painter("Hans", "Knipedol", null),
                        null, false),
                new Painting("", null, null, false),
                null);
    }

    @Test
    void testNestedThreeLevelsDeepNonNullCheck() {
        Painting paintingContainingNulls = new Painting("", new Painter("Hans", "Knipedol", null),
                null, false);

        List<Museum> listContainingNestedNulls = Arrays.asList(
                new Museum("", null, Arrays.asList(paintingContainingNulls)),
                null);

        final List<Museum> expected = TestSampleGenerator.getMuseumListContainingNulls();
        final List<Museum> containingNulls = Stream.concat(listContainingNestedNulls.stream(), expected.stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("containingNulls = " + containingNulls);

        final List<Museum> actual = containingNulls.stream()
                .filter(nonNull(Museum::getMostPopularPainting, Painting::painter, Painter::getDateOfBirth))
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    void testMapTwoLevelsDeepNullSafe() {
        final List<Painting> paintingListContainingNestedNulls = getPaintingListContainingNestedNulls();
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final List<Painting> paintings = Stream.concat(paintingListContainingNestedNulls.stream(), paintingList.stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("paintings = " + paintings);

        final List<LocalDate> expected = paintings.stream()
                .filter(Objects::nonNull)
                .map(Painting::painter)
                .filter(Objects::nonNull)
                .map(Painter::getDateOfBirth)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        final List<LocalDate> painterDateOfBirthList = paintings.stream()
                .flatMap(nullSafe(Painting::painter, Painter::getDateOfBirth))
                .collect(Collectors.toList());

        assertEquals(expected, painterDateOfBirthList);
    }

    @Test
    void testMapThreeLevelsDeepNullSafe() {
        final List<Museum> concatenatedMuseumList = getMuseumsContainingNulls();

        System.out.println("concatenatedList = " + concatenatedMuseumList);

        final List<DayOfWeek> expected = concatenatedMuseumList.stream()
                .filter(Objects::nonNull)
                .flatMap(museum -> museum.getPaintings().stream())
                .filter(Objects::nonNull)
                .map(Painting::painter)
                .filter(Objects::nonNull)
                .map(Painter::getDateOfBirth)
                .filter(Objects::nonNull)
                .map(LocalDate::getDayOfWeek)
                .collect(Collectors.toList());

        final List<DayOfWeek> listOfDayOfWeekBirthDateMostPopularPaintingPainters = concatenatedMuseumList.stream()
                .flatMap(StreamUtils::streamOf)
                .flatMap(nullSafe(
                        Painting::painter,
                        Painter::getDateOfBirth,
                        LocalDate::getDayOfWeek))
                .collect(Collectors.toList());

        System.out.println("listOfDayOfWeekBirthDateMostPopularPaintingPainters = " + listOfDayOfWeekBirthDateMostPopularPaintingPainters);

        assertEquals(expected, listOfDayOfWeekBirthDateMostPopularPaintingPainters);
    }

    @Test
    void testMapFourLevelsDeepNullSafe() {
        final List<Museum> concatenatedMuseumList = getMuseumsContainingNulls();

        System.out.println("concatenatedList = " + concatenatedMuseumList);

        final List<DayOfWeek> expected = concatenatedMuseumList.stream()
                .filter(Objects::nonNull)
                .map(Museum::getMostPopularPainting)
                .map(Painting::painter)
                .filter(Objects::nonNull)
                .map(Painter::getDateOfBirth)
                .filter(Objects::nonNull)
                .map(LocalDate::getDayOfWeek)
                .collect(Collectors.toList());

        final List<DayOfWeek> actual = concatenatedMuseumList.stream()
                .flatMap(nullSafe(
                        Museum::getMostPopularPainting,
                        Painting::painter,
                        Painter::getDateOfBirth,
                        LocalDate::getDayOfWeek))
                .collect(Collectors.toList());

        final List<DayOfWeek> expectedDaysOfWeek = Arrays.asList(
                DayOfWeek.WEDNESDAY,
                LocalDate.of(1632, Month.OCTOBER, 31).getDayOfWeek(),
                LocalDate.of(1853, Month.MARCH, 20).getDayOfWeek(),
                LocalDate.of(1881, Month.OCTOBER, 25).getDayOfWeek());

        System.out.println("expectedDaysOfWeek = " + expectedDaysOfWeek);

        assertAll(
                () -> assertEquals(expectedDaysOfWeek, actual),
                () -> assertEquals(expected, actual)
        );
    }

    @Test
    void testMapFourLevelsDeepNullSafeWithMapMulti() {
        final List<Museum> concatenatedMuseumList = getMuseumsContainingNulls();

        System.out.println("concatenatedList = " + concatenatedMuseumList);

        final List<DayOfWeek> expected = concatenatedMuseumList.stream()
                .filter(Objects::nonNull)
                .map(Museum::getMostPopularPainting)
                .map(Painting::painter)
                .filter(Objects::nonNull)
                .map(Painter::getDateOfBirth)
                .filter(Objects::nonNull)
                .map(LocalDate::getDayOfWeek)
                .collect(Collectors.toList());

        final List<DayOfWeek> expectedDaysOfWeek = Arrays.asList(
                DayOfWeek.WEDNESDAY,
                LocalDate.of(1632, Month.OCTOBER, 31).getDayOfWeek(),
                LocalDate.of(1853, Month.MARCH, 20).getDayOfWeek(),
                LocalDate.of(1881, Month.OCTOBER, 25).getDayOfWeek());

        System.out.println("expectedDaysOfWeek = " + expectedDaysOfWeek);

                assertEquals(expectedDaysOfWeek, expected);
    }

    private List<Museum> getMuseumsContainingNulls() {
        Painting paintingContainingNulls = new Painting("", new Painter("Hans", "Knipedol", null),
                null, false);

        List<Museum> listContainingNestedNulls = Arrays.asList(
                new Museum("", null, Arrays.asList(paintingContainingNulls)),
                null);

        final List<Museum> museums = TestSampleGenerator.getMuseumListContainingNulls();
        return Stream.concat(listContainingNestedNulls.stream(), museums.stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Test
    void testStreamOfIterable() {
        final Painter hans = new Painter("Hans", "Zuidervaart", LocalDate.of(1989, Month.OCTOBER, 18));
        final Painting my_creation = new Painting("My creation at 5", hans, Year.of(1994), false);
        final Painting my_other_creation = new Painting("My other creation at 7", hans, Year.of(1997), false);

        hans.addPaintings(my_creation, my_other_creation);

        final List<String> names = streamOf(hans)
                .filter(by(Painting::yearOfCreation, isBefore(1995)))
                .map(Painting::name)
                .collect(Collectors.toList());

        names.forEach(System.out::println);

        assertEquals(1, names.size());
    }

}
