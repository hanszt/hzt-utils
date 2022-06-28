package org.hzt.utils.strings;

import org.hzt.utils.PreConditions;
import org.hzt.utils.Transformable;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.comparables.ComparableX;
import org.hzt.utils.numbers.BigDecimalX;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.numbers.LongX;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("squid:S1448")
public final class StringX implements CharSequence, Sequence<Character>, Transformable<StringX>, ComparableX<StringX> {

    private final String string;

    private StringX(String string) {
        this.string = string;
    }

    private StringX(CharSequence charSequence) {
        this(String.valueOf(charSequence));
    }

    private StringX(Iterable<Character> characterIterable) {
        string = Sequence.of(characterIterable).joinToString("");
    }

    private StringX(char[] charArray) {
        this(new String(charArray));
    }

    private StringX(byte[] bytes) {
        this(bytes, StandardCharsets.UTF_8);
    }

    private StringX(byte[] bytes, Charset charset) {
        this(new String(bytes, charset));
    }

    public static StringX of(String s) {
        return new StringX(s);
    }

    public static StringX of(CharSequence s) {
        return new StringX(s);
    }

    public static StringX of(Iterable<Character> characterIterable) {
        return new StringX(characterIterable);
    }

    public static StringX of(char @NotNull [] s) {
        return new StringX(s);
    }

    public static StringX of(char @NotNull [] data, int offset, int count) {
        return StringX.of(String.valueOf(data, offset, count));
    }

    public static StringX of(char first, char... chars) {
        char[] charsArray = new char[chars.length + 1];
        charsArray[0] = first;
        System.arraycopy(chars, 0, charsArray, 1, charsArray.length - 1);
        return new StringX(charsArray);
    }

    public static StringX of(byte[] s) {
        return new StringX(s);
    }

    public static StringX of(byte[] s, Charset charset) {
        return new StringX(s, charset);
    }

    @SafeVarargs
    public static <T> StringX of(T... values) {
        return Sequence.of(values).joinToStringX();
    }

    @SafeVarargs
    public static <T> StringX of(CharSequence delimiter, T... values) {
        return Sequence.of(values).joinToStringX(delimiter);
    }

    public static StringX of(Object obj) {
        return StringX.of(String.valueOf(obj));
    }

    public static StringX of(boolean b) {
        return StringX.of(String.valueOf(b));
    }

    public static StringX of(char c) {
        return StringX.of(String.valueOf(c));
    }

    public static StringX of(int i) {
        return StringX.of(String.valueOf(i));
    }

    public static StringX of(long l) {
        return StringX.of(String.valueOf(l));
    }

    public static StringX of(float f) {
        return StringX.of(String.valueOf(f));
    }

    public static StringX of(double d) {
        return StringX.of(String.valueOf(d));
    }

    public static String capitalized(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public StringX plus(String s) {
        return plus(StringX.of(s)).joinToStringX("");
    }

    public StringX reversed() {
        return StringX.of(new StringBuilder(string).reverse());
    }

    public StringX replaceFirstChar(UnaryOperator<Character> replacer) {
        char[] charArray = toCharArray();
        if (charArray.length > 0) {
            charArray[0] = replacer.apply(charArray[0]);
        }
        return StringX.of(charArray);
    }

    @NotNull
    private Iterator<Character> charIterator() {
        return new Iterator<>() {

            private int index = 0;
            private final char[] charArray = string.toCharArray();

            @Override
            public boolean hasNext() {
                return index < charArray.length;
            }

            @Override
            public Character next() {
                int prevIndex = index;
                if (prevIndex >= charArray.length) {
                    throw new NoSuchElementException();
                }
                return charArray[index++];
            }
        };
    }

    @Override
    public int length() {
        return string.length();
    }

    @SuppressWarnings("all")
    public boolean isEmpty() {
        return string.isEmpty();
    }

    public StringX ifEmpty(Supplier<CharSequence> defaultCsSupplier) {
        return isEmpty() ? StringX.of(defaultCsSupplier.get()) : this;
    }

    public boolean isBlank() {
        return string.isBlank();
    }

    public StringX ifBlank(Supplier<CharSequence> defaultStringSupplier) {
        return isBlank() ? StringX.of(defaultStringSupplier.get()) : this;
    }

    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }

    public int codePointAt(int index) {
        return string.codePointAt(index);
    }

    public int codePointBefore(int index) {
        return string.codePointBefore(index);
    }

    public int codePointCount(int beginIndex, int endIndex) {
        return string.codePointCount(beginIndex, endIndex);
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return string.offsetByCodePoints(index, codePointOffset);
    }

    public void getChars(int srcBegin, int srcEnd, char @NotNull [] dst, int dstBegin) {
        string.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public byte[] getBytes(@NotNull Charset charset) {
        return string.getBytes(charset);
    }

    public byte[] getBytes() {
        return string.getBytes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StringX that = (StringX) o;
        return Objects.equals(string, that.string);
    }

    public boolean contentEquals(@NotNull CharSequence cs) {
        return string.contentEquals(cs);
    }

    public boolean equalsIgnoreCase(CharSequence another) {
        return string.equalsIgnoreCase(StringX.of(another).toString());
    }

    public int compareTo(@NotNull CharSequence another) {
        return string.compareTo(StringX.of(another).toString());
    }

    public int compareToIgnoreCase(@NotNull CharSequence str) {
        return string.compareToIgnoreCase(StringX.of(str).toString());
    }

    public boolean regionMatches(int toffset, @NotNull String other, int ooffset, int len) {
        return string.regionMatches(toffset, other, ooffset, len);
    }

    public boolean regionMatches(boolean ignoreCase, int toffset, @NotNull String other, int ooffset, int len) {
        return string.regionMatches(ignoreCase, toffset, other, ooffset, len);
    }

    public boolean startsWith(@NotNull CharSequence prefix, int toffset) {
        return string.startsWith(StringX.of(prefix).toString(), toffset);
    }

    public boolean startsWith(@NotNull CharSequence prefix) {
        return string.startsWith(StringX.of(prefix).toString());
    }

    public boolean endsWith(@NotNull CharSequence suffix) {
        return string.endsWith(StringX.of(suffix).toString());
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    public int indexOf(int ch) {
        return string.indexOf(ch);
    }

    public int indexOf(int ch, int fromIndex) {
        return string.indexOf(ch, fromIndex);
    }

    public int lastIndexOf(int ch) {
        return string.lastIndexOf(ch);
    }

    public int lastIndexOf(int ch, int fromIndex) {
        return string.lastIndexOf(ch, fromIndex);
    }

    public int indexOf(@NotNull CharSequence str) {
        return string.indexOf(StringX.of(str).toString());
    }

    public int indexOf(@NotNull CharSequence str, int fromIndex) {
        return string.indexOf(StringX.of(str).toString(), fromIndex);
    }

    public int lastIndexOf(@NotNull CharSequence str) {
        return string.lastIndexOf(StringX.of(str).toString());
    }

    public int lastIndexOf(@NotNull CharSequence str, int fromIndex) {
        return string.lastIndexOf(StringX.of(str).toString(), fromIndex);
    }

    public StringX substring(int beginIndex) {
        return StringX.of(string.substring(beginIndex));
    }

    public StringX substring(int beginIndex, int endIndex) {
        return StringX.of(string.substring(beginIndex, endIndex));
    }

    @NotNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return string.subSequence(start, end);
    }

    public StringX concat(@NotNull CharSequence charSequence) {
        return StringX.of(string.concat(StringX.of(charSequence).toString()));
    }

    @Override
    public StringX filter(@NotNull Predicate<? super Character> predicate) {
        return StringX.of(Sequence.super.filter(predicate));
    }

    public StringX replace(char oldChar, char newChar) {
        return StringX.of(string.replace(oldChar, newChar));
    }

    public boolean matches(@NotNull Pattern regex) {
        return regex.matcher(string).matches();
    }

    public boolean contains(@NotNull CharSequence s) {
        return string.contains(s);
    }

    public StringX replaceFirst(@NotNull Pattern regex, @NotNull CharSequence replacement) {
        return StringX.of(string.replaceFirst(regex.toString(), StringX.of(replacement).toString()));
    }

    public StringX replaceAll(@NotNull Pattern regex, @NotNull String replacement) {
        return StringX.of(string.replaceAll(regex.toString(), replacement));
    }

    public StringX replace(@NotNull CharSequence target, @NotNull CharSequence replacement) {
        return StringX.of(string.replace(target, replacement));
    }

    public ListX<String> split(@NotNull Pattern regex) {
        return ListX.of(string.split(regex.toString()));
    }

    public ListX<String> split(@NotNull Pattern regex, int limit) {
        return ListX.of(string.split(regex.toString(), limit));
    }

    public ListX<String> split(@NotNull CharSequence delimiter) {
        return split(0, delimiter);
    }

    public ListX<String> split(int limit, @NotNull CharSequence delimiter) {
        final var delimiterAsString = StringX.of(delimiter).toString();
        return split(delimiterAsString, limit);
    }

    /**
     * @param delimiter the delimiter
     * @param limit the limit to which to split
     * @return a list with split strings
     *
     * This method is inspired by the Kotlin standard library
     */
    private ListX<String> split(String delimiter, int limit) {
        PreConditions.require(limit >= 0, () -> "Limit must be non-negative, but was " + limit);
        var currentOffset = 0;
        var nextIndex = indexOf(delimiter, currentOffset);
        if (nextIndex == -1 || limit == 1) {
            return ListX.of(this.toString());
        }

        boolean isLimited = limit > 0;
        MutableListX<String> result = MutableListX.withInitCapacity(isLimited ? Math.min(limit, 10) : 10);
        do {
            result.add(string.substring(currentOffset, nextIndex));
            currentOffset = nextIndex + delimiter.length();
            // Do not search for next occurrence if we're reaching limit
            if (isLimited && result.size() == limit - 1) {
                break;
            }
            nextIndex = indexOf(delimiter, currentOffset);
        } while (nextIndex != -1);

        result.add(string.substring(currentOffset));
        return result;
    }

    public static StringX join(CharSequence delimiter, CharSequence... elements) {
        return StringX.of(String.join(delimiter, elements));
    }

    public static StringX join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
        return StringX.of(String.join(delimiter, elements));
    }

    public StringX toLowerCase(@NotNull Locale locale) {
        return StringX.of(string.toLowerCase(locale));
    }

    public StringX toLowerCase() {
        return StringX.of(string.toLowerCase());
    }

    public StringX toUpperCase(@NotNull Locale locale) {
        return StringX.of(string.toUpperCase(locale));
    }

    public StringX toUpperCase() {
        return StringX.of(string.toUpperCase());
    }

    public StringX trim() {
        return StringX.of(string.trim());
    }

    public boolean isAnagramOf(CharSequence other) {
        return trim().replace(" ", "").toLowerCase().group()
                .equals(StringX.of(other).trim().replace(" ", "").toLowerCase().group());
    }

    public IntX toIntX(int radix) {
        return IntX.of(Integer.parseInt(string, radix));
    }

    public IntX toIntX() {
        return toIntX(10);
    }

    public int toInt(int radix) {
        return Integer.parseInt(string, radix);
    }

    public int toInt() {
        return Integer.parseInt(string);
    }

    public LongX toLongX() {
        return LongX.of(Long.parseLong(string));
    }

    public long toLong() {
        return Long.parseLong(string);
    }

    public DoubleX toDoubleX() {
        return DoubleX.of(Double.parseDouble(string));
    }

    public double toDouble() {
        return Double.parseDouble(string);
    }

    public BigDecimalX toBigDecimalX() {
        return BigDecimalX.of(new BigDecimal(string));
    }

    @Override
    public @NotNull String toString() {
        return string;
    }

    @NotNull
    @Override
    public IntStream codePoints() {
        return string.codePoints();
    }

    public char[] toCharArray() {
        return string.toCharArray();
    }

    public static StringX format(@NotNull String format, Object... args) {
        return StringX.of(String.format(format, args));
    }

    public static StringX format(Locale l, @NotNull String format, Object... args) {
        return StringX.of(String.format(l, format, args));
    }

    public static StringX copyOf(char @NotNull [] data, int offset, int count) {
        return StringX.of(String.copyValueOf(data, offset, count));
    }

    public static StringX copyOf(char @NotNull [] data) {
        return StringX.of(String.copyValueOf(data));
    }

    public byte[] getBytes(@NotNull String charsetName) throws UnsupportedEncodingException {
        return string.getBytes(charsetName);
    }

    public boolean contentEquals(@NotNull StringBuffer sb) {
        return string.contentEquals(sb);
    }

    public boolean equalsIgnoreCase(String anotherString) {
        return string.equalsIgnoreCase(anotherString);
    }

    public int compareTo(@NotNull String anotherString) {
        return string.compareTo(anotherString);
    }

    public int compareToIgnoreCase(@NotNull String str) {
        return string.compareToIgnoreCase(str);
    }

    public boolean startsWith(@NotNull String prefix, int toffset) {
        return string.startsWith(prefix, toffset);
    }

    public boolean startsWith(@NotNull String prefix) {
        return string.startsWith(prefix);
    }

    public boolean endsWith(@NotNull String suffix) {
        return string.endsWith(suffix);
    }

    public int indexOf(@NotNull String str) {
        return string.indexOf(str);
    }

    public int indexOf(@NotNull String str, int fromIndex) {
        return string.indexOf(str, fromIndex);
    }

    public int lastIndexOf(@NotNull String str) {
        return string.lastIndexOf(str);
    }

    public int lastIndexOf(@NotNull String str, int fromIndex) {
        return string.lastIndexOf(str, fromIndex);
    }

    public StringX concat(@NotNull String str) {
        return StringX.of(string.concat(str));
    }

    public boolean matches(@NotNull String regex) {
        return string.matches(regex);
    }

    public StringX replaceFirst(@NotNull String regex, @NotNull String replacement) {
        return StringX.of(string.replaceFirst(regex, replacement));
    }

    public String replaceAll(@NotNull String regex, @NotNull String replacement) {
        return string.replaceAll(regex, replacement);
    }

    public StringX strip() {
        return StringX.of(string.strip());
    }

    public StringX stripLeading() {
        return StringX.of(string.stripLeading());
    }

    public StringX stripTrailing() {
        return StringX.of(string.stripTrailing());
    }

    public Stream<StringX> linesAsStream() {
        return string.lines().map(StringX::of);
    }

    public Sequence<StringX> lines() {
        return Sequence.of(linesAsStream().collect(Collectors.toList()));
    }

    public <R> R transformString(Function<? super String, ? extends R> f) {
        return f.apply(string);
    }

    public StringX formatted(Object... args) {
        return StringX.of(String.format(string, args));
    }

    public Stream<Character> boxedChars() {
        return chars().mapToObj(c -> (char) c);
    }

    public StringX repeat(int count) {
        return StringX.of(string.repeat(count));
    }

    @Override
    public @NotNull StringX get() {
        return this;
    }

    @NotNull
    @Override
    public Iterator<Character> iterator() {
        return charIterator();
    }

    @Override
    public int compareTo(@NotNull StringX o) {
        return string.compareTo(o.string);
    }
}
