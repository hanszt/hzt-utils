package org.hzt.utils.strings;

import org.hzt.utils.Patterns;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class StringXTest {

    @Test
    void testGroupStringX() {
        final var hallo = "hallo";

        final var expected = groupByChars(hallo)
                .values().stream()
                .map(List::size)
                .collect(Collectors.toList());

        final var characterCounts = StringX.of(hallo)
                .group()
                .values()
                .map(ListX::size);

        println("hallo = " + characterCounts);

        assertIterableEquals(expected, characterCounts);
    }

    @Test
    void testReplaceFirstChar() {
        final var hallo = StringX.of("hallo")
                .replaceFirstChar(c -> 'H').toString();
        assertEquals("Hallo", hallo);
    }

    @Test
    void testStringXPlus() {
        final var stringX = StringX.of("Hallo").plus("Raar");
        assertEquals("HalloRaar", stringX.toString());
    }

    @Test
    void testStringXToListXThenFirstAndLast() {
        final var characters = StringX.of("Hello").toListX();

        assertAll(
                () -> assertEquals('H', characters.first()),
                () -> assertEquals('o', characters.last())
        );
    }

    @Test
    void testStringXOfCharIterable() {
        final var characters = List.of('H', 'e', 'y', '!', '1', '2', '3');

        assertEquals("Hey!123", StringX.ofChars(characters).toString());
    }

    @Test
    void testStringChainingX() {
        final var characters = List.of('H', 'e', 'y', '!', '1', '2', '3');

        final var actual = StringX.ofChars(characters)
                .concat("\nHallo")
                .replaceFirst("lo", "asd")
                .lines()
                .toTwo(Sequence::count, s -> s.joinToString(""));

        assertAll(
                () -> assertEquals("Hey!123Halasd", actual.second()),
                () -> assertEquals(2, actual.first()));
    }

    @Test
    void testIfEmpty() {
        assertEquals("Test", StringX.of("").ifEmpty(() -> "Test").toString());
    }

    @Test
    void testIfBlank() {
        assertEquals("Test", StringX.of("  ").ifBlank(() -> "Test").toString());
    }

    @Test
    void flatMapToCharArrayAndFromCharArrayToStringX() {
        final var characters = Sequence.of("hallo", "Wat is dat?", "Een test")
                .joinToStringX("")
                .toCharArray();

        final var stringX = StringX.of(characters);

        assertEquals("halloWat is dat?Een test", stringX.toString());
    }

    @ParameterizedTest
    @MethodSource("anagrams")
    void testStringsAreEachOthersAnagram(final String string1, final String string2) {
        assertAll(
                () -> assertTrue(StringX.of(string1).isAnagramOf(string2)),
                () -> assertTrue(isAnagram(string1, string2))
        );
    }

    private static Sequence<Arguments> anagrams() {
        return Sequence.of(
                arguments("bevoordelen", "voorbeelden"),
                arguments("Laptop machines", "Apple Macintosh"),
                arguments("Avida Dollars", "Salvador Dali"),
                arguments("Altissimvm planetam tergeminvm observavi", "Salve vmbistinevm geminatvm Martia proles"),
                arguments("O DRACONIAN DEVIL", "LEONARDO DA VINCI"),
                arguments("Tom Marvolo Riddle", "I am Lord Voldemort")
        );
    }

    @ParameterizedTest
    @CsvSource({
            "bevoordelen, bevoorraden",
            "Laptop machines, Microsoft windows",
            "Avida Dollars, Salvador Dalis",
            "Altissimum planetam tergeminum observavi, Salve umbistineum geminatum Martia proles"})
    void testStringIsNotAnagram(final String string1, final String string2) {
        assertAll(
                () -> assertFalse(StringX.of(string1).isAnagramOf(string2)),
                () -> assertFalse(isAnagram(string1, string2))
        );
    }

    private static boolean isAnagram(final String s1, final String s2) {
        final var parsed1 = s1.trim().toLowerCase(Locale.ROOT).replace(" ", "");
        final var parsed2 = s2.trim().toLowerCase(Locale.ROOT).replace(" ", "");
        final var grouping1 = groupByChars(parsed1);
        final var grouping2 = groupByChars(parsed2);
        return grouping1.equals(grouping2);
    }

    private static Map<Character, List<Character>> groupByChars(final String s1) {
        return s1.chars()
                .mapToObj(i -> (char) i)
                .collect(Collectors.groupingBy(Function.identity()));
    }

    @ParameterizedTest
    @CsvSource({
            "this is a test, This is a test",
            "hANS, Hans",
            "1233, 1233",
            "THIS IS A LITTLE TO MUCH!, This is a little to much!"})
    void testCapitalized(final String input, final String expected) {

        final var actual = StringX.capitalized(input);

        assertEquals(expected, actual);
    }

    @Nested
    class StringSplittingTests {

        @Test
        void testSplitToSequence() {
            final var string = "hallo, this, is, a, test -> answer";
            final var comma = StringX.of(", ");
            final var strings = StringX.of(string).splitToSequence(comma, " -> ");

            strings.forEach(System.out::println);

            assertIterableEquals(Sequence.of("hallo", "this", "is", "a", "test", "answer"), strings);
        }

        @Test
        void testSplitToSequenceIgnoreCase() {
            final var string = "hallo O this o is O a, test -> answer";
            final var oDelimiter = new StringBuilder(" o ");
            final var strings = StringX.of(string).splitToSequence(true, ", ", oDelimiter, " -> ");

            strings.forEach(System.out::println);

            assertIterableEquals(Sequence.of("hallo", "this", "is", "a", "test", "answer"), strings);
        }

        @Test
        void splitToSequenceByPattern() {
            final var strings = StringX.of("test@test.com      this is some text")
                    .splitToSequence(Patterns.blankStringPattern)
                    .toList();

            System.out.println("strings.size() = " + strings.size());

            assertEquals(List.of("test@test.com", "this", "is", "some", "text"), strings);
        }

        @Test
        void testStringTokenizerVsSplitToSequence() {
            final var testString = "This is a\ftest\tcontaining\ndefault\rseparators";

            final var tokenizer = new StringTokenizer(testString);
            final List<String> tokens = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                tokens.add(tokenizer.nextToken());
            }

            final var strings = Sequence.of(() -> new StringTokenizer(testString).asIterator())
                    .map(Object::toString)
                    .toListX();

            final var split = StringX.of(testString).split(" ", "\t", "\n", "\r", "\f");

            final var expected = ListX.of("This", "is", "a", "test", "containing", "default", "separators");

            assertAll(
                    () -> assertEquals(expected, split),
                    () -> assertEquals(expected, strings),
                    () -> assertEquals(expected, ListX.of(tokens))
            );
        }

        @Test
        void testSplitWithoutDelimitersDoesNotSplitInputString() {
            final var testString = "This is a\ftest\tcontaining\ndefault\rseparators";

            final var split = StringX.of(testString).split();

            final var expected = ListX.of(testString);

            assertEquals(expected, split);
        }

        @Test
        void testSplitToSequenceByEmptyStringYieldsSeparateCharacters() {
            final var testString = "Test";

            final var split = StringX.of(testString).splitToSequence("");

            final var expected = Sequence.of("", "T", "e", "s", "t", "");

            assertIterableEquals(expected, split);
        }

        @Test
        void testSplitByEmptyStringYieldsSeparateCharacters() {
            final var testString = "Test";

            final var split1 = StringX.of(testString).split("");

            final var split2 = Pattern.compile("").splitAsStream(testString)
                    .collect(Collectors.toList());
            split2.add(0, "");
            split2.add("");

            final var expected = ListX.of("", "T", "e", "s", "t", "");

            assertAll(
                    () -> assertEquals(expected, split1),
                    () -> assertEquals(expected, ListX.of(split2))
            );
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"This is a string", "abbreviate", "Another string"})
    void testAbbreviate(final String string) {
        final var MAX_LENGTH = 12;
        final var abbreviate = StringX.of(string).abbreviate(MAX_LENGTH);

        println(abbreviate);

        assertTrue(abbreviate.length() <= MAX_LENGTH);
    }
    
    @Nested
    class ContainsTests {

        @ParameterizedTest
        @ValueSource(chars = {'a', 't', 'c', 'm', '!', 'T'})
        void testStringContainsChars(final char c) {
            final var s = "This is a test string to test contains method!";
            assertTrue(StringX.of(s).contains(c));
        }

        @ParameterizedTest
        @ValueSource(chars = {'A', '?', 'b', 'z', 'M'})
        void testStringDoesNotContainChars(final char c) {
            final var s = "This is a test string to test contains method!";
            assertFalse(StringX.of(s).contains(c));
        }
    }

    @Nested
    class OverridenSequenceMethodTests {

        @Test
        void testFilterCharNrs() {
            final var s = StringX.of("This 1s a strin9 containing s0me nrs: 4")
                    .filter(Character::isDigit)
                    .skip(1)
                    .toInt();

            assertEquals(904, s);
        }
    }
}
