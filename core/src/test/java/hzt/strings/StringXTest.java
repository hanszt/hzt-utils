package hzt.strings;

import hzt.collections.ListX;
import hzt.sequences.Sequence;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringXTest {

    @Test
    void testGroupStringX() {
        final String hallo = "hallo";

        final List<Integer> expected = groupByChars(hallo)
                .values().stream()
                .map(List::size)
                .collect(Collectors.toList());

        final ListX<Integer> characterCounts = StringX.of(hallo)
                .group()
                .values()
                .map(ListX::size);

        It.println("hallo = " + characterCounts);

        assertIterableEquals(expected, characterCounts);
    }

    @Test
    void testReplaceFirstChar() {
        final String hallo = StringX.of("hallo")
                .replaceFirstChar(c -> 'H').toString();
        assertEquals("Hallo", hallo);
    }

    @Test
    void testStringXPlus() {
        final StringX stringX = StringX.of("Hallo").plus("Raar");
        assertEquals("HalloRaar", stringX.toString());
    }

    @Test
    void testStringXToListXThenFirstAndLast() {
        final ListX<Character> characters = StringX.of("Hello").toListX();

        assertAll(
                () -> assertEquals('H', characters.first()),
                () -> assertEquals('o', characters.last())
        );
    }

    @Test
    void testStringXOfCharIterable() {
        final List<Character> characters = Arrays.asList('H', 'e', 'y', '!', '1', '2', '3');

        assertEquals("Hey!123", StringX.of(characters).toString());
    }

    @Test
    void testStringChainingX() {
        final List<Character> characters = Arrays.asList('H', 'e', 'y', '!', '1', '2', '3');

        final Pair<Long, String> actual = StringX.of(characters)
                .concat("Hallo")
                .replaceFirst("lo", "asd")
                .toTwo(Sequence::count, s -> s.joinToString(""));

        assertAll(
                () -> assertEquals("Hey!123Halasd", actual.second()),
                () -> assertEquals(13, actual.first()));
    }

    @Test
    void testIfEmpty() {
        assertEquals("Test", StringX.of("").ifEmpty(() -> "Test").toString());
    }

    @Test
    void flatMapToCharArrayAndFromCharArrayToStringX() {
        final char[] characters = Sequence.of("hallo", "Wat is dat?", "Een test")
                .joinToStringX("")
                .toCharArray();

        final StringX stringX = StringX.of(characters);

        assertEquals("halloWat is dat?Een test", stringX.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "bevoordelen -> voorbeelden",
            "Laptop machines ->  Apple Macintosh",
            "Avida Dollars ->  Salvador Dali",
            "Altissimvm planetam tergeminvm observavi -> Salve vmbistinevm geminatvm Martia proles"})
    void testStringIsAnagram(String string) {
        final ListX<String> split = StringX.of(string).split(" -> ");
        final String string1 = split.first();
        final String string2 = split.last();

        assertTrue(StringX.of(string1).isAnagramOf(string2));
        assertTrue(isAnagram(string1, string2));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "bevoordelen -> bevoorraden",
            "Laptop machines ->  Microsoft windows",
            "Avida Dollars ->  Salvador Dalis",
            "Altissimum planetam tergeminum observavi -> Salve umbistineum geminatum Martia proles"})
    void testStringIsNotAnagram(String string) {
        final ListX<String> split = StringX.of(string).split(" -> ");
        final String string1 = split.first();
        final String string2 = split.last();

        assertFalse(StringX.of(string1).isAnagramOf(string2));
        assertFalse(isAnagram(string1, string2));
    }

    private static boolean isAnagram(String s1, String s2) {
        final String parsed1 = s1.trim().toLowerCase(Locale.ROOT).replace(" ", "");
        final String parsed2 = s2.trim().toLowerCase(Locale.ROOT).replace(" ", "");
        final Map<Character, List<Character>> grouping1 = groupByChars(parsed1);
        final Map<Character, List<Character>> grouping2 = groupByChars(parsed2);
        return grouping1.equals(grouping2);
    }

    private static Map<Character, List<Character>> groupByChars(String s1) {
        return s1.chars()
                .mapToObj(i -> (char) i)
                .collect(Collectors.groupingBy(Function.identity()));
    }
}
