package hzt.strings;

import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.utils.It;
import hzt.utils.Transformable;
import hzt.iterables.IterableX;
import hzt.numbers.IntX;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public final class StringX implements CharSequence, IterableX<Character>, Transformable<StringX> {

    private final String string;

    private StringX(String string) {
        this.string = string;
    }

    private StringX(CharSequence charSequence) {
        this(String.valueOf(charSequence));
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

    public static StringX of(char[] s) {
        return new StringX(s);
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

    public StringX plus(String s) {
        return plus(StringX.of(s)).joinToStringX();
    }

    public StringX reversed() {
        return StringX.of(new StringBuilder(string).reverse());
    }

    public ListX<Character> toListX() {
        return toMutableList();
    }

    public MutableListX<Character> toMutableList() {
        return toMutableListOf(It::self);
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
        return new Iterator<Character>() {

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

    public boolean isEmpty() {
        return string.isEmpty();
    }

    public StringX ifEmpty(Supplier<String> defaultStringSupplier) {
        return isEmpty() ? StringX.of(defaultStringSupplier.get()) : this;
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

    public void getChars(int srcBegin, int srcEnd, @NotNull char[] dst, int dstBegin) {
        string.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public byte[] getBytes(@NotNull String charsetName) throws UnsupportedEncodingException {
        return string.getBytes(charsetName);
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

    public boolean contentEquals(@NotNull StringBuffer sb) {
        return string.contentEquals(sb);
    }

    public boolean contentEquals(@NotNull CharSequence cs) {
        return string.contentEquals(cs);
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

    public boolean regionMatches(int toffset, @NotNull String other, int ooffset, int len) {
        return string.regionMatches(toffset, other, ooffset, len);
    }

    public boolean regionMatches(boolean ignoreCase, int toffset, @NotNull String other, int ooffset, int len) {
        return string.regionMatches(ignoreCase, toffset, other, ooffset, len);
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

    public StringX concat(@NotNull String str) {
        return StringX.of(string.concat(str));
    }

    public StringX replace(char oldChar, char newChar) {
        return StringX.of(string.replace(oldChar, newChar));
    }

    public boolean matches(@NotNull String regex) {
        return string.matches(regex);
    }

    public boolean contains(@NotNull CharSequence s) {
        return string.contains(s);
    }

    public StringX replaceFirst(@NotNull String regex, @NotNull String replacement) {
        return StringX.of(string.replaceFirst(regex, replacement));
    }

    public StringX replaceAll(@NotNull String regex, @NotNull String replacement) {
        return StringX.of(string.replaceAll(regex, replacement));
    }

    public StringX replace(@NotNull CharSequence target, @NotNull CharSequence replacement) {
        return StringX.of(string.replace(target, replacement));
    }

    public ListX<String> split(@NotNull String regex, int limit) {
        return ListX.of(string.split(regex, limit));
    }

    public ListX<String> split(@NotNull String regex) {
        return ListX.of(string.split(regex));
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

    public boolean isAnagramOf(String other) {
        final MapX<Character, MutableListX<Character>> group1 = groupedCharacters(this);
        final MapX<Character, MutableListX<Character>> group2 = groupedCharacters(StringX.of(other));
        return group1.equals(group2);
    }

    private static MapX<Character, MutableListX<Character>> groupedCharacters(StringX s1) {
        final StringX trimmedAndNoSpaces = s1.trim().replace(" ", "").toLowerCase();
        return trimmedAndNoSpaces.group();
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

    public IntX toIntX(int radix) {
       return IntX.of(Integer.parseInt(string, radix));
    }

    public IntX toIntX() {
        return toIntX(10);
    }

    @Override
    public @NotNull String toString() {
        return joinToString();
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

    public static StringX valueOf(Object obj) {
        return StringX.of(String.valueOf(obj));
    }

    public static StringX valueOf(@NotNull char[] data) {
        return StringX.of(String.valueOf(data));
    }

    public static StringX valueOf(@NotNull char[] data, int offset, int count) {
        return StringX.of(String.valueOf(data, offset, count));
    }

    public static StringX copyValueOf(@NotNull char[] data, int offset, int count) {
        return StringX.of(String.copyValueOf(data, offset, count));
    }

    public static StringX copyValueOf(@NotNull char[] data) {
        return StringX.of(String.copyValueOf(data));
    }

    public static StringX valueOf(boolean b) {
        return StringX.of(String.valueOf(b));
    }

    public static StringX valueOf(char c) {
        return StringX.of(String.valueOf(c));
    }

    public static StringX valueOf(int i) {
        return StringX.of(String.valueOf(i));
    }

    public static StringX valueOf(long l) {
        return StringX.of(String.valueOf(l));
    }

    public static StringX valueOf(float f) {
        return StringX.of(String.valueOf(f));
    }

    public static StringX valueOf(double d) {
        return StringX.of(String.valueOf(d));
    }

    @Override
    public StringX get() {
        return this;
    }

    @NotNull
    @Override
    public Iterator<Character> iterator() {
        return charIterator();
    }
}
