package org.hzt.utils.strings;

import org.hzt.utils.Transformable;
import org.hzt.utils.collections.CollectionX;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.comparables.ComparableX;
import org.hzt.utils.iterables.Reversable;
import org.hzt.utils.numbers.BigDecimalX;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.numbers.LongX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.tuples.IndexedValue;
import org.hzt.utils.tuples.IntPair;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hzt.utils.PreConditions.require;

@SuppressWarnings({"squid:S1448", "squid:S1200", "squid:S4351"})
public final class StringX implements CharSequence, Sequence<Character>, Transformable<StringX>, ComparableX<StringX>, Reversable<StringX> {

    private final String string;

    private StringX(final String string) {
        this.string = string;
    }

    private StringX(final CharSequence charSequence) {
        this(charSequence.toString());
    }

    private StringX(final Iterable<Character> characterIterable) {
        string = Sequence.of(characterIterable).joinToString("");
    }

    private StringX(final char[] charArray) {
        this(new String(charArray));
    }

    private StringX(final byte[] bytes) {
        this(bytes, StandardCharsets.UTF_8);
    }

    private StringX(final byte[] bytes, final Charset charset) {
        this(new String(bytes, charset));
    }

    public static StringX of(final String s) {
        return new StringX(s);
    }

    public static StringX of(final CharSequence s) {
        return new StringX(s);
    }

    public static StringX ofChars(final Iterable<Character> characterIterable) {
        return new StringX(characterIterable);
    }

    public static StringX of(final char[] s) {
        return new StringX(s);
    }

    public static StringX of(final char[] data, final int offset, final int count) {
        return StringX.of(String.valueOf(data, offset, count));
    }

    public static StringX of(final char first, final char... chars) {
        final var charsArray = new char[chars.length + 1];
        charsArray[0] = first;
        System.arraycopy(chars, 0, charsArray, 1, charsArray.length - 1);
        return new StringX(charsArray);
    }

    public static StringX of(final byte... s) {
        return new StringX(s);
    }

    public static StringX of(final byte[] s, final Charset charset) {
        return new StringX(s, charset);
    }

    @SafeVarargs
    public static <T> StringX of(final T... values) {
        return Sequence.of(values).joinToStringX();
    }

    @SafeVarargs
    public static <T> StringX of(final CharSequence delimiter, final T... values) {
        return Sequence.of(values).joinToStringX(delimiter);
    }

    public static StringX of(final Object obj) {
        return StringX.of(String.valueOf(obj));
    }

    public static StringX of(final boolean b) {
        return StringX.of(String.valueOf(b));
    }

    public static StringX of(final char c) {
        return StringX.of(String.valueOf(c));
    }

    public static StringX of(final int i) {
        return StringX.of(String.valueOf(i));
    }

    public static StringX of(final long l) {
        return StringX.of(String.valueOf(l));
    }

    public static StringX of(final float f) {
        return StringX.of(String.valueOf(f));
    }

    public static StringX of(final double d) {
        return StringX.of(String.valueOf(d));
    }

    public static String capitalized(final String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public StringX plus(final String s) {
        return StringX.of(string.concat(s));
    }

    public StringX reversed() {
        return StringX.of(new StringBuilder(string).reverse());
    }

    public StringX replaceFirstChar(final UnaryOperator<Character> replacer) {
        final var charArray = toCharArray();
        if (charArray.length > 0) {
            charArray[0] = replacer.apply(charArray[0]);
        }
        return StringX.of(charArray);
    }

    public StringX abbreviate(final int maxLength) {
        final var tail = "...";
        final var n = maxLength - tail.length();
        return StringX.of(codePointSequence()
                .take(n)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint))
                .plus(tail);
    }

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
                final var prevIndex = index;
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

    public StringX ifEmpty(final Supplier<CharSequence> defaultCsSupplier) {
        return isEmpty() ? StringX.of(defaultCsSupplier.get()) : this;
    }

    public boolean isBlank() {
        return string.isBlank();
    }

    public StringX ifBlank(final Supplier<CharSequence> defaultStringSupplier) {
        return isBlank() ? StringX.of(defaultStringSupplier.get()) : this;
    }

    @Override
    public char charAt(final int index) {
        return string.charAt(index);
    }

    public int codePointAt(final int index) {
        return string.codePointAt(index);
    }

    public int codePointBefore(final int index) {
        return string.codePointBefore(index);
    }

    public int codePointCount(final int beginIndex, final int endIndex) {
        return string.codePointCount(beginIndex, endIndex);
    }

    public int offsetByCodePoints(final int index, final int codePointOffset) {
        return string.offsetByCodePoints(index, codePointOffset);
    }

    public void getChars(final int srcBegin, final int srcEnd, final char[] dst, final int dstBegin) {
        string.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public byte[] getBytes(final Charset charset) {
        return string.getBytes(charset);
    }

    public byte[] getBytes() {
        return string.getBytes();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var that = (StringX) o;
        return Objects.equals(string, that.string);
    }

    public boolean contentEquals(final CharSequence cs) {
        return string.contentEquals(cs);
    }

    public boolean equalsIgnoreCase(final CharSequence another) {
        return string.equalsIgnoreCase(StringX.of(another).toString());
    }

    public int compareTo(final CharSequence another) {
        return string.compareTo(StringX.of(another).toString());
    }

    public int compareToIgnoreCase(final CharSequence str) {
        return string.compareToIgnoreCase(StringX.of(str).toString());
    }

    public boolean regionMatches(final int toffset, final String other, final int ooffset, final int len) {
        return string.regionMatches(toffset, other, ooffset, len);
    }

    public boolean regionMatches(final boolean ignoreCase, final int toffset, final String other, final int ooffset, final int len) {
        return string.regionMatches(ignoreCase, toffset, other, ooffset, len);
    }

    public boolean startsWith(final CharSequence prefix, final int toffset) {
        return string.startsWith(StringX.of(prefix).toString(), toffset);
    }

    public boolean startsWith(final CharSequence prefix) {
        return string.startsWith(StringX.of(prefix).toString());
    }

    public boolean endsWith(final CharSequence suffix) {
        return string.endsWith(StringX.of(suffix).toString());
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    public int indexOf(final int ch) {
        return string.indexOf(ch);
    }

    public int indexOf(final int ch, final int fromIndex) {
        return string.indexOf(ch, fromIndex);
    }

    public int lastIndexOf(final int ch) {
        return string.lastIndexOf(ch);
    }

    public int lastIndexOf(final int ch, final int fromIndex) {
        return string.lastIndexOf(ch, fromIndex);
    }

    public int indexOf(final CharSequence str) {
        return string.indexOf(StringX.of(str).toString());
    }

    public int indexOf(final CharSequence str, final int fromIndex) {
        return string.indexOf(StringX.of(str).toString(), fromIndex);
    }

    public int lastIndexOf(final CharSequence str) {
        return string.lastIndexOf(StringX.of(str).toString());
    }

    public int lastIndexOf(final CharSequence str, final int fromIndex) {
        return string.lastIndexOf(StringX.of(str).toString(), fromIndex);
    }

    public StringX substring(final int beginIndex) {
        return StringX.of(string.substring(beginIndex));
    }

    public StringX substring(final int beginIndex, final int endIndex) {
        return StringX.of(string.substring(beginIndex, endIndex));
    }

    @Override
    public CharSequence subSequence(final int start, final int end) {
        return string.subSequence(start, end);
    }

    public StringX concat(final CharSequence charSequence) {
        return StringX.of(string.concat(StringX.of(charSequence).toString()));
    }

    @Override
    public StringX filter(final Predicate<? super Character> predicate) {
        return StringX.ofChars(Sequence.super.filter(predicate));
    }

    public StringX replace(final char oldChar, final char newChar) {
        return StringX.of(string.replace(oldChar, newChar));
    }

    public boolean matches(final Pattern regex) {
        return regex.matcher(string).matches();
    }

    public boolean contains(final CharSequence s) {
        return string.contains(s);
    }

    public boolean contains(final char c) {
        return string.indexOf(c) >= 0;
    }

    public StringX replaceFirst(final Pattern regex, final CharSequence replacement) {
        return StringX.of(string.replaceFirst(regex.toString(), StringX.of(replacement).toString()));
    }

    public StringX replaceAll(final Pattern regex, final String replacement) {
        return StringX.of(string.replaceAll(regex.toString(), replacement));
    }

    public StringX replace(final CharSequence target, final CharSequence replacement) {
        return StringX.of(string.replace(target, replacement));
    }

    public ListX<String> split(final Pattern regex) {
        return ListX.of(string.split(regex.toString()));
    }

    public ListX<String> split(final Pattern regex, final int limit) {
        return ListX.of(string.split(regex.toString(), limit));
    }

    public ListX<String> split(final CharSequence... delimiters) {
        return splitToSequence(false, delimiters).toListX();
    }

    public Sequence<String> splitToSequence(final CharSequence... delimiters) {
        return splitToSequence(false, delimiters);
    }

    public Sequence<String> splitToSequence(final boolean ignoreCase, final CharSequence... delimiters) {
        return splitToSequence(ignoreCase, 0, delimiters);
    }
    public Sequence<String> splitToSequence(final boolean ignoreCase,
                                            final int limit,
                                            final CharSequence... delimiters) {
        return rangeDelimitedBy(string, delimiters, ignoreCase, limit)
                .map(range -> string.substring(range.start(), range.endInclusive() + 1));
    }

    public Sequence<String> splitToSequence(final Pattern pattern) {
        return Sequence.of(pattern.splitAsStream(string)::iterator);
    }

    private static Sequence<IntRange> rangeDelimitedBy(final String string,
                                                       final CharSequence[] delimiters,
                                                       final boolean ignoreCase,
                                                       final int limit) {
        require(limit >= 0, () -> "Limit must be non-negative, but was " + limit);
        return new DelimitedRangesSequence(string, 0, limit,
                (charSequence, curIndex) -> findAnyOf(delimiters, ignoreCase, charSequence, curIndex));
    }

    private static IntPair findAnyOf(final CharSequence[] delimiters, final boolean ignoreCase, final CharSequence s, final int curIndex) {
        final var indexedValue = findAnyOf(s, curIndex, ListX.of(delimiters), ignoreCase);
        return indexedValue != null ? IntPair.of(indexedValue.index(), indexedValue.value().length()) : null;
    }

    private static IndexedValue<CharSequence> findAnyOf(final CharSequence charSequence,
                                                  final int startIndex,
                                                  final CollectionX<CharSequence> delimiters,
                                                  final boolean ignoreCase) {
        final var stringX = StringX.of(charSequence);
        if (!ignoreCase && delimiters.size() == 1) {
            final var singleString = delimiters.single();
            final var index = stringX.indexOf(singleString, startIndex);
            return (index < 0) ? null : new IndexedValue<>(index, singleString);
        }

        final var indices = IntRange.closed(Math.max(startIndex, 0), charSequence.length());

        for (final var iterator = indices.iterator(); iterator.hasNext(); ) {
            final var index = iterator.nextInt();
            final var matchingCharSequence = delimiters
                    .findFirst(charSeq -> charSeq.toString().regionMatches(ignoreCase, 0, stringX.string, index, charSeq.length()))
                    .orElse(null);
            if (matchingCharSequence != null) {
                return new IndexedValue<>(index, matchingCharSequence);
            }
        }
        return null;
    }

    public static StringX join(final CharSequence delimiter, final CharSequence... elements) {
        return StringX.of(String.join(delimiter, elements));
    }

    public static StringX join(final CharSequence delimiter, final Iterable<? extends CharSequence> elements) {
        return StringX.of(String.join(delimiter, elements));
    }

    public StringX toLowerCase(final Locale locale) {
        return StringX.of(string.toLowerCase(locale));
    }

    public StringX toLowerCase() {
        return StringX.of(string.toLowerCase());
    }

    public StringX toUpperCase(final Locale locale) {
        return StringX.of(string.toUpperCase(locale));
    }

    public StringX toUpperCase() {
        return StringX.of(string.toUpperCase());
    }

    public StringX trim() {
        return StringX.of(string.trim());
    }

    public boolean isAnagramOf(final CharSequence other) {
        return trim().replace(" ", "").toLowerCase().group()
                .equals(StringX.of(other).trim().replace(" ", "").toLowerCase().group());
    }

    public IntX toIntX(final int radix) {
        return IntX.of(Integer.parseInt(string, radix));
    }

    public IntX toIntX() {
        return toIntX(10);
    }

    public int toInt(final int radix) {
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
    public String toString() {
        return string;
    }

    @Override
    public IntStream codePoints() {
        return string.codePoints();
    }

    public IntSequence codePointSequence() {
        return IntSequence.of(codePoints());
    }

    @Override
    public IntStream chars() {
        return string.codePoints();
    }

    public char[] toCharArray() {
        return string.toCharArray();
    }

    public static StringX format(final String format, final Object... args) {
        return StringX.of(String.format(format, args));
    }

    public static StringX format(final Locale l, final String format, final Object... args) {
        return StringX.of(String.format(l, format, args));
    }

    public static StringX copyOf(final char[] data, final int offset, final int count) {
        return StringX.of(String.copyValueOf(data, offset, count));
    }

    public static StringX copyOf(final char[] data) {
        return StringX.of(String.copyValueOf(data));
    }

    public byte[] getBytes(final String charsetName) throws UnsupportedEncodingException {
        return string.getBytes(charsetName);
    }

    public boolean contentEquals(final StringBuffer sb) {
        return string.contentEquals(sb);
    }

    public boolean equalsIgnoreCase(final String anotherString) {
        return string.equalsIgnoreCase(anotherString);
    }

    public int compareTo(final String anotherString) {
        return string.compareTo(anotherString);
    }

    public int compareToIgnoreCase(final String str) {
        return string.compareToIgnoreCase(str);
    }

    public boolean startsWith(final String prefix, final int toffset) {
        return string.startsWith(prefix, toffset);
    }

    public boolean startsWith(final String prefix) {
        return string.startsWith(prefix);
    }

    public boolean endsWith(final String suffix) {
        return string.endsWith(suffix);
    }

    public int indexOf(final String str) {
        return string.indexOf(str);
    }

    public int indexOf(final String str, final int fromIndex) {
        return string.indexOf(str, fromIndex);
    }

    public int lastIndexOf(final String str) {
        return string.lastIndexOf(str);
    }

    public int lastIndexOf(final String str, final int fromIndex) {
        return string.lastIndexOf(str, fromIndex);
    }

    public StringX concat(final String str) {
        return StringX.of(string.concat(str));
    }

    public boolean matches(final String regex) {
        return string.matches(regex);
    }

    public StringX replaceFirst(final String regex, final String replacement) {
        return StringX.of(string.replaceFirst(regex, replacement));
    }

    public String replaceAll(final String regex, final String replacement) {
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
        return Sequence.of(linesAsStream().toList());
    }

    public StringX indent(final int n) {
        return StringX.of(string.indent(n));
    }

    public StringX stripIndent() {
        return StringX.of(string.stripIndent());
    }

    public StringX translateEscapes() {
        return StringX.of(string.translateEscapes());
    }

    public StringX formatted(final Object... args) {
        return StringX.of(string.formatted(args));
    }

    public Stream<Character> boxedChars() {
        return chars().mapToObj(c -> (char) c);
    }

    public StringX repeat(final int count) {
        return StringX.of(string.repeat(count));
    }

    public Optional<String> describeConstable() {
        return string.describeConstable();
    }

    public String resolveConstantDesc(final MethodHandles.Lookup lookup) {
        return string.resolveConstantDesc(lookup);
    }

    @Override
    public StringX get() {
        return this;
    }

    @Override
    public Iterator<Character> iterator() {
        return charIterator();
    }

    //overriden sequence methods
    @Override
    public StringX plus(final Character value) {
        return StringX.ofChars(Sequence.super.plus(value));
    }

    @Override
    public StringX minus(final Character value) {
        return StringX.ofChars(Sequence.super.minus(value));
    }

    @Override
    public StringX intersperse(final Character value) {
        return StringX.ofChars(Sequence.super.intersperse(value));
    }

    @Override
    public StringX onEach(final Consumer<? super Character> consumer) {
        return StringX.ofChars(Sequence.super.onEach(consumer));
    }

    @Override
    public StringX distinct() {
        return StringX.ofChars(Sequence.super.distinct());
    }

    @Override
    public StringX take(final long n) {
        return StringX.ofChars(Sequence.super.take(n));
    }

    @Override
    public StringX takeWhile(final Predicate<? super Character> predicate) {
        return StringX.ofChars(Sequence.super.takeWhile(predicate));
    }

    @Override
    public StringX skip(final long n) {
        return StringX.ofChars(Sequence.super.skip(n));
    }

    @Override
    public StringX skipWhile(final Predicate<? super Character> predicate) {
        return StringX.ofChars(Sequence.super.skipWhile(predicate));
    }

    @Override
    public int compareTo(final StringX o) {
        return string.compareTo(o.string);
    }
}
