package org.hzt.utils.iterables;

import org.hzt.utils.strings.StringX;

import java.util.Iterator;
import java.util.function.Function;

@FunctionalInterface
public interface Stringable<T> extends Iterable<T> {

    default String joinToString() {
        return joinToStringBy(t -> t != null ? t.toString() : null);
    }

    default String joinToString(final CharSequence delimiter) {
        return joinToStringBy(Object::toString, delimiter);
    }

    default <R> String joinToStringBy(final Function<? super T, ? extends R> selector) {
        return joinToStringBy(selector, ", ");
    }

    default <R> String joinToStringBy(final Function<? super T, ? extends R> selector, final CharSequence delimiter) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            final R r = selector.apply(iterator.next());
            sb.append(r).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }

    default StringX joinToStringX() {
        return joinToStringXBy(Object::toString);
    }

    default StringX joinToStringX(final CharSequence delimiter) {
        return joinToStringXBy(Object::toString, delimiter);
    }

    default <R> StringX joinToStringXBy(final Function<? super T, ? extends R> selector) {
        return joinToStringXBy(selector, ", ");
    }

    default <R> StringX joinToStringXBy(final Function<? super T, ? extends R> selector, final CharSequence delimiter) {
        return StringX.of(joinToStringBy(selector, delimiter));
    }
}
