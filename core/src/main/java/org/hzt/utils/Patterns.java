package org.hzt.utils;

import org.hzt.utils.sequences.Sequence;

import java.util.regex.Pattern;

public final class Patterns {

    public static final Pattern commaPattern = Pattern.compile(",");
    public static final Pattern arrowPattern = Pattern.compile(" -> ");
    public static final Pattern pipePattern = Pattern.compile("\\| ");
    public static final Pattern blankStringPattern = Pattern.compile("\\s+", Pattern.UNICODE_CHARACTER_CLASS);

    /**
     * The pattern to use for validating an email address
     *
     * @see <a href="https://www.baeldung.com/java-email-validation-regex">Email Validation in Java<a/>
     */
    @SuppressWarnings({"squid:S5998", "squid:S5867"})
    public static final Pattern emailPattern = Pattern.compile(
            "^(?=.{1,64}@)[A-Za-z\\d_-]+(\\.[A-Za-z\\d_-]+)*@[^-][A-Za-z\\d-]+(\\.[A-Za-z\\d-]+)*(\\.[A-Za-z]{2,})$");

    private Patterns() {
    }

    public static Sequence<String> splitToSequence(Pattern pattern, CharSequence charSequence) {
        return Sequence.of(pattern.splitAsStream(charSequence)::iterator);
    }
}
