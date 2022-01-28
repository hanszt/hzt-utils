package hzt.strings;

import hzt.collections.ListX;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringXTest {

    @Test
    void testToStringX() {
        final String hallo = "hallo";

        final List<Integer> expected = groupByChars(hallo)
                .values().stream()
                .map(List::size)
                .collect(Collectors.toList());

        final ListX<Integer> characterCounts = StringX.of(hallo).group().valuesToListXOf(List::size);

        System.out.println("hallo = " + characterCounts);

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
