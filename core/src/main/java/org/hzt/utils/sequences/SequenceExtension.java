package org.hzt.utils.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@FunctionalInterface
public interface SequenceExtension<T, R> {

    Sequence<R> extend(Sequence<T> sequence);

    default <V> SequenceExtension<T, V> andThen(@NotNull SequenceExtension<R, V> after) {
        Objects.requireNonNull(after);
        return tSequence -> after.extend(extend(tSequence));
    }

    default <V> SequenceExtension<V, R> compose(@NotNull SequenceExtension<V, T> before) {
        Objects.requireNonNull(before);
        return vSequence -> extend(before.extend(vSequence));
    }
}
